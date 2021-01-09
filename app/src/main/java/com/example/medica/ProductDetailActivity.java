package com.example.medica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.medica.Model.Products;
import com.example.medica.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity {

    public String likeCheck = "Unchecked";

    FloatingActionButton addToCart;
    ImageView productImage;
    ElegantNumberButton numberButton;
    TextView productPrice, productDescription, productName,likeCount;

    String productID = "";
    SparkButton sparkButton;
    String productImg="";

    int like = 0;

    Button comments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        productID = getIntent().getStringExtra("pid");

        productImage = findViewById(R.id.product_image_details);
        addToCart = findViewById(R.id.add_product_to_cart);
        numberButton = findViewById(R.id.number_btn);
        productPrice = findViewById(R.id.product_price_details);
        productDescription = findViewById(R.id.product_description_details);
        productName = findViewById(R.id.product_name_details);
        sparkButton = findViewById(R.id.spark_button);
        likeCount = findViewById(R.id.likecount);
        comments = findViewById(R.id.comments);
        
        getProductDetails(productID);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });

        getLikes(productID);
        getOnlineStatus(Prevalent.currentOnlineUser.getPhone());



        sparkButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState){

                        like = like + 1;
                        final DatabaseReference pExtraLike = FirebaseDatabase.getInstance().getReference().child("Product Extras").child(productID).child("Likes");
                        pExtraLike.setValue(like);
                        likeCheck = "Checked";
                    final DatabaseReference onLineStatus = FirebaseDatabase.getInstance().getReference().child("Online Status").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(productID).child("Likes");
                    onLineStatus.setValue(likeCheck);
                }
                else {
                    like = like - 1;
                    final DatabaseReference pExtraLike = FirebaseDatabase.getInstance().getReference().child("Product Extras").child(productID).child("Likes");
                    pExtraLike.setValue(like);
                    likeCheck = "Unchecked";

                    final DatabaseReference onLineStatus = FirebaseDatabase.getInstance().getReference().child("Online Status").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(productID).child("Likes");
                    onLineStatus.setValue(likeCheck);
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });


        setlikecheck(Prevalent.currentOnlineUser.getPhone());

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String myMessage = productID;
                bundle.putString("message", myMessage );

                BottomSheetComments bottomSheetComments = new BottomSheetComments();
                bottomSheetComments.setArguments(bundle);
                bottomSheetComments.show(getSupportFragmentManager(),"TAG");
            }
        });


    }

    private void setlikecheck(String phone) {
        final DatabaseReference onLineStatus = FirebaseDatabase.getInstance().getReference().child("Online Status").child(phone).child("Products").child(productID).child("Likes");
        onLineStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getValue(String.class).equals("Checked"))
                {
                    sparkButton.setChecked(true);
                    Toast.makeText(ProductDetailActivity.this, "Like On 1", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getOnlineStatus(String phone) {
        //Getting Like Status
        final DatabaseReference onLineStatus = FirebaseDatabase.getInstance().getReference().child("Online Status").child(phone).child("Products").child(productID).child("Likes");
        onLineStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    likeCheck = snapshot.getValue(String.class);
                }
                else {
                    onLineStatus.setValue(likeCheck);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private int getLikes(String productID) {
        final DatabaseReference pExtraLike = FirebaseDatabase.getInstance().getReference().child("Product Extras").child(productID).child("Likes");
        pExtraLike.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    like = snapshot.getValue(Integer.class);
                    likeCount.setText(like+"");

                }
                else {
                    Toast.makeText(ProductDetailActivity.this, "Not Exist", Toast.LENGTH_SHORT).show();
                    pExtraLike.setValue(like);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return like;
    }


    private void addingToCartList()
    {
        String saveCurrentTime, saveCurrentDate;

        Calendar calforDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calforDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calforDate.getTime());

        int tPrice = Integer.parseInt(numberButton.getNumber()) * Integer.parseInt(productPrice.getText().toString().replace("₹",""));

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartmap = new HashMap<>();
        cartmap.put("pid", productID);
        cartmap.put("pName", productName.getText().toString());
        cartmap.put("price", productPrice.getText().toString());
        cartmap.put("date", saveCurrentDate);
        cartmap.put("time", saveCurrentTime);
        cartmap.put("quantity", numberButton.getNumber());
        cartmap.put("totalPrice",tPrice);
        cartmap.put("discount","");
        cartmap.put("img", productImg);

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                            .child("Products").child(productID)
                            .updateChildren(cartmap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ProductDetailActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });




    }

    private void getProductDetails(final String productID)
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Products products = snapshot.getValue(Products.class);

                    productName.setText("|" + products.getName());
                    productDescription.setText(products.getDesc());
                    productPrice.setText("₹" + products.getPrice());
                    Picasso.get().load(products.getImg()).into(productImage);
                    productImg = products.getImg();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}