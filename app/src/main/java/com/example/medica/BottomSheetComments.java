package com.example.medica;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medica.Model.Comments;
import com.example.medica.Model.Products;
import com.example.medica.Prevalent.Prevalent;
import com.example.medica.ViewHolder.CommentsViewHolder;
import com.example.medica.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BottomSheetComments extends BottomSheetDialogFragment {

    String myValue; //This Contains product id
    EditText userComment;
    FloatingActionButton commentSend;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Comments, CommentsViewHolder> adapter;

    DatabaseReference databaseReference;
    DatabaseReference commentRef;

    String Message;



    public BottomSheetComments() {
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.comments, container, false);
       myValue = this.getArguments().getString("message");  //This Contains product id

        recyclerView = view.findViewById(R.id.commentsRecView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());   //Hope so error
        recyclerView.setLayoutManager(layoutManager);





//        TextView textView = view.findViewById(R.id.test);
//        textView.setText(myValue);

        commentRef = FirebaseDatabase.getInstance().getReference().child("Product Extras").child("Comments").child(myValue);

        recyclerViewLoader();


        userComment = view.findViewById(R.id.userComment);
        commentSend = view.findViewById(R.id.commentsend);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Product Extras").child("Comments")
                .child(myValue);



        commentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference bWords = FirebaseDatabase.getInstance().getReference().child("Bad Words");

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, YYYY");
                String saveCurrentDate = currentDate.format(calendar.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                String saveCurrentTime = currentTime.format(calendar.getTime());
                final String msgTime = saveCurrentDate + saveCurrentTime;

                Message = userComment.getText().toString();
                if (!TextUtils.isEmpty(Message)){

                    final String[] allWords  = Message.split(" ");


                        bWords.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot s : snapshot.getChildren()){

                                    if (Message.contains(s.getValue(String.class))) {
                                      //  Toast.makeText(getContext(), "ahaa1", Toast.LENGTH_SHORT).show();
                                        Message = Message.replace(s.getValue(String.class), "****");
                                        databaseReference.child(Prevalent.currentOnlineUser.getPhone() + msgTime).child("Com").setValue(Message);
                                    }
                                    else {
                                        databaseReference.child(Prevalent.currentOnlineUser.getPhone() + msgTime).child("Com").setValue(Message);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });






                    databaseReference.child(Prevalent.currentOnlineUser.getPhone() + msgTime).child("Phone")
                            .setValue(Prevalent.currentOnlineUser.getPhone());
                    databaseReference.child(Prevalent.currentOnlineUser.getPhone() + msgTime).child("Name")
                            .setValue(Prevalent.currentOnlineUser.getName());
                    databaseReference.child(Prevalent.currentOnlineUser.getPhone() + msgTime).child("Img")
                            .setValue(Prevalent.currentOnlineUser.getImage());
                    databaseReference.child(Prevalent.currentOnlineUser.getPhone() + msgTime).child("Cid")
                            .setValue(Prevalent.currentOnlineUser.getPhone() + msgTime);

                }
                userComment.setText("");
                adapter.notifyDataSetChanged();


            }
        });



        return view;

    }

    private void recyclerViewLoader() {

        FirebaseRecyclerOptions<Comments> options =
                new FirebaseRecyclerOptions.Builder<Comments>()
                        .setQuery(commentRef, Comments.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Comments, CommentsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CommentsViewHolder commentsViewHolder, int i, @NonNull final Comments comments) {

                commentsViewHolder.Name_Usr.setText(comments.getName());
                commentsViewHolder.Comments_Usr.setText(comments.getCom());
                Picasso.get().load(comments.getImg())
                        .placeholder(R.drawable.comuser)
                        .into(commentsViewHolder.userImage);

                //Interactive comments

                commentsViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        if (comments.getPhone().equals(Prevalent.currentOnlineUser.getPhone()
                        )){
                            Toast.makeText(getContext(), "My Comment", Toast.LENGTH_SHORT).show();

                            CharSequence options[] = new CharSequence[]{
                                    //"Edit",
                                    "Remove"
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Comments: ");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0){
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Product Extras")
                                                .child("Comments").child(myValue).child(comments.getCid());

                                        reference.removeValue();
                                    }
                                }
                            });

                                builder.show();

                        }





                        return false;


                    }
                });



            }

            @NonNull
            @Override
            public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commentholder, parent, false);
                CommentsViewHolder holder = new CommentsViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);

        adapter.startListening();


    }
}
