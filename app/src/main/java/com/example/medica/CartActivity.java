package com.example.medica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medica.Model.Cart;
import com.example.medica.Prevalent.Prevalent;
import com.example.medica.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button NextProcessButton, Remove;
    TextView txtTotalAmount, noOfItems,productTotal;
    String pid = "";

    long tProducts =0;
    int to = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProcessButton = findViewById(R.id.next_process_button);
        noOfItems = findViewById(R.id.no_of_items);

        txtTotalAmount = findViewById(R.id.total_price);
        productTotal = findViewById(R.id.product_total);
       // Remove = findViewById(R.id.remove);

//        NextProcessButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (to != 0){
//                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
//                intent.putExtra("Total", to);
//                startActivity(intent);
//            }else {
//                    Toast.makeText(CartActivity.this, "No Items in the cart", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        });




    }


    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartlistref = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart> ()
                .setQuery(cartlistref.child("User View")
                        .child(Prevalent.currentOnlineUser.getPhone()).child("Products"), Cart.class).build();

        final FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, final int i, @NonNull final Cart cart) {

                cartViewHolder.txtProductQuantity.setText("Quantity = "+ cart.getQuantity());
                cartViewHolder.txtProductPrice.setText("Price = " + cart.getPrice());
                cartViewHolder.txtProductName.setText(cart.getpName().replace("|",""));
                Picasso.get().load(cart.getImg()).into(cartViewHolder.pImage);
              //  cartViewHolder.productTotalPRice.setText(getInt(cart.getPrice()) * getInt(cart.getQuantity()) + "");
                cartViewHolder.productTotalPRice.setText("= ₹" +cart.getTotalPrice()+ "");
                cartViewHolder.Remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cartlistref.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(cart.getPid())
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(CartActivity.this, "Item removed", Toast.LENGTH_SHORT).show();
//                                                        Intent intent = new Intent(CartActivity.this, ShopPage.class);
//                                                        startActivity(intent);
                                        }
                                    }
                                });
                    }
                });

                cartViewHolder.checkOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                        intent.putExtra("pid", " |Quantity = "+cart.getQuantity()+"| |pid = "+cart.getPid() + "|");
                        intent.putExtra("totalAmount1", cart.getTotalPrice());
                        intent.putExtra("name", cart.getpName());
                        startActivity(intent);

                        Toast.makeText(CartActivity.this, "Clicked", Toast.LENGTH_SHORT).show();

                    }
                });

                //Toast.makeText(CartActivity.this, cart.getPid(), Toast.LENGTH_SHORT).show();
               // Toast.makeText(CartActivity.this, getImgUrl(cart.getPid()) + "58", Toast.LENGTH_SHORT).show();

                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Edit",
                                "Remove"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options: ");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0){
                                    Intent intent = new Intent(CartActivity.this, ProductDetailActivity.class);
                                    intent.putExtra("pid", cart.getPid());
                                    startActivity(intent);
                                }
                                if (which == 1){
                                    cartlistref.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(cart.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(CartActivity.this, "Item removed", Toast.LENGTH_SHORT).show();
//                                                        Intent intent = new Intent(CartActivity.this, ShopPage.class);
//                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });





                
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                noOfItems.setText(getItemCount() + " Items");

                getTotalPrice();
            }
        };



        recyclerView.setAdapter(adapter);
        adapter.startListening();


       // getTotalItems();





    }

    private void getTotalPrice() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                to = 0;
                for (DataSnapshot s : snapshot.getChildren()){
                    to = to + s.child("totalPrice").getValue(Integer.class);

                }
                //Toast.makeText(CartActivity.this, to + "", Toast.LENGTH_SHORT).show();
                productTotal.setText("Rs " + to);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTotalItems() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products");

    }

    private int getInt(String s) {
        int a = Integer.parseInt(s.replace("₹","").replace(" ",""));
        return a;
    }


}