package com.example.medica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medica.Model.Orders;
import com.example.medica.Prevalent.Prevalent;
import com.example.medica.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminAllOrders extends AppCompatActivity {

    public  RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    int cardsizestatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_orders);

        recyclerView = findViewById(R.id.adminOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Admin Orders");

//        FirebaseRecyclerOptions<Orders> options = new FirebaseRecyclerOptions.Builder<Orders> ()
//                .setQuery(ref.child("User View")
//                        .child(Prevalent.currentOnlineUser.getPhone()).child("Products"), Cart.class).build();

        FirebaseRecyclerOptions<Orders> options = new FirebaseRecyclerOptions.Builder<Orders>()
                .setQuery(ref, Orders.class).build();


        final FirebaseRecyclerAdapter<Orders, OrderViewHolder> adapter = new FirebaseRecyclerAdapter<Orders, OrderViewHolder>(options) {
            @SuppressLint("ResourceAsColor")
            @Override
            protected void onBindViewHolder(@NonNull final OrderViewHolder orderViewHolder, int i, @NonNull final Orders orders) {



                orderViewHolder.pName_orders.setText("Products: " + orders.getpName().replaceAll("\\|"," \n"));
                orderViewHolder.name_order.setText("Buyer: " + orders.getName());
                orderViewHolder.amount_orders.setText("Amount: " + orders.getTotalAmont()+ "");
                orderViewHolder.address_orders.setText("Address: " + orders.getAddress());
                orderViewHolder.phone_orders.setText("Phone: " + orders.getPhone());
                orderViewHolder.date_orders.setText("Date: " + orders.getDate());
                orderViewHolder.orderStatus.setText("Status: " + orders.getState());
                orderViewHolder.productOrderID.setText("Order ID: " + orders.getOrderID());


                if (i%4 == 0){
                    orderViewHolder.cardView.setCardBackgroundColor(Color.parseColor("#bedcfa"));
                }
                else if (i%4 == 1){
                    orderViewHolder.cardView.setCardBackgroundColor(Color.parseColor("#98acf8"));
                }
                else if (i%4 == 2){
                    orderViewHolder.cardView.setCardBackgroundColor(Color.parseColor("#b088f9"));
                }
                else {
                    orderViewHolder.cardView.setCardBackgroundColor(Color.parseColor("#da9ff9"));
                }

//                cardsizestatus = 0;

                orderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        if (cardsizestatus == 0){
                            orderViewHolder.pName_orders.setVisibility(View.VISIBLE);
                            orderViewHolder.name_order.setVisibility(View.VISIBLE);
                            orderViewHolder.amount_orders.setVisibility(View.VISIBLE);
                            orderViewHolder.address_orders.setVisibility(View.VISIBLE);
                            orderViewHolder.phone_orders.setVisibility(View.VISIBLE);
                            orderViewHolder.date_orders.setVisibility(View.VISIBLE);
                            orderViewHolder.productOrderID.setVisibility(View.VISIBLE);
                            cardsizestatus = 1;
                        }
                        else {
                            orderViewHolder.name_order.setVisibility(View.GONE);
                            orderViewHolder.amount_orders.setVisibility(View.GONE);
                            orderViewHolder.address_orders.setVisibility(View.GONE);
                            orderViewHolder.phone_orders.setVisibility(View.GONE);
                            orderViewHolder.date_orders.setVisibility(View.GONE);
                            orderViewHolder.productOrderID.setVisibility(View.GONE);
                            cardsizestatus = 0;
                        }



                    }
                });

                orderViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        String p = orders.getDate()+" "+ orders.getTime();

                        Intent intent = new Intent(AdminAllOrders.this, OrderStatus.class);
                        intent.putExtra("orderidreal", p);
                        intent.putExtra("phno", orders.getPhone());
                        startActivity(intent);

                        return false;

                    }
                });





            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
                OrderViewHolder holder = new OrderViewHolder(view);
                return holder;
            }






        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}




