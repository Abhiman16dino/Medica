package com.example.medica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.medica.Model.Products;
import com.example.medica.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminAllProducts extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;
    DatabaseReference productRef;

    TextView adminallprosearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_products);

        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.rViewAdminAll);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adminallprosearch = findViewById(R.id.adminallprosearch);

        adminallprosearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminAllProducts.this, SearchProductsActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);

                Animatoo.animateFade(AdminAllProducts.this);
            }
        });

        recyclerViewloader();

    }

    private void recyclerViewloader() {

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(productRef, Products.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {



                productViewHolder.pName.setText(products.getName());
                productViewHolder.pDesc.setText(products.getDesc());
                productViewHolder.pPrice.setText("₹"+ products.getPrice());

                Picasso.get().load(products.getImg())
                        .placeholder(R.drawable.imgplaceholder)
                        .into(productViewHolder.pImage);

                productViewHolder.bNow.setText("Edit");
                productViewHolder.bNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AdminAllProducts.this, AdminEditProducts.class);
                        intent.putExtra("data",products.getPid());
                        startActivity(intent);



                    }
                });


//                productViewHolder.bNow.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(ShopPage.this, ConfirmFinalOrderActivity.class);
//                        intent.putExtra("pid", " |Quantity = "+1+"| |pid = "+products.getPid() + "|");
//                        intent.putExtra("totalAmount1", Integer.parseInt(products.getPrice()));
//                        intent.putExtra("name", products.getName());
//                        startActivity(intent);
//                    }
//                });

                //here i set clicklistner on each cardview

//                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(ShopPage.this, ProductDetailActivity.class);
//                        intent.putExtra("pid", products.getPid());
//                        startActivity(intent);
//                    }
//                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);

        adapter.startListening();



    }


}