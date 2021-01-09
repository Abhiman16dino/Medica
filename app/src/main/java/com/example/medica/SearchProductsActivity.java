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
import android.widget.EditText;
import android.widget.Toast;

import com.example.medica.Model.Products;
import com.example.medica.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchProductsActivity extends AppCompatActivity {

    Button searchBtn;
    EditText inputText;
    RecyclerView searchList;
    String SearchInput = "";
    String intentdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        inputText = findViewById(R.id.search_product_name);
        searchBtn = findViewById(R.id.search_btn);
        searchList = findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));

         intentdata = getIntent().getStringExtra("Admin");

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchInput = inputText.getText().toString();
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!intentdata.equals("Admin")) {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

            FirebaseRecyclerOptions<Products> options =
                    new FirebaseRecyclerOptions.Builder<Products>()
                            .setQuery(reference.orderByChild("name").startAt(SearchInput), Products.class)
                            .build();

            FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                    new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {
                            productViewHolder.pName.setText(products.getName());
                            productViewHolder.pDesc.setText(products.getDesc());
                            productViewHolder.pPrice.setText("₹" + products.getPrice());
                            productViewHolder.bNow.setVisibility(View.GONE);

                            Picasso.get().load(products.getImg())
                                    .placeholder(R.drawable.imgplaceholder)
                                    .into(productViewHolder.pImage);

                            productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SearchProductsActivity.this, ProductDetailActivity.class);
                                    intent.putExtra("pid", products.getPid());
                                    startActivity(intent);
                                }
                            });
                        }

                        @NonNull
                        @Override
                        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                            ProductViewHolder holder = new ProductViewHolder(view);
                            return holder;
                        }
                    };
            searchList.setAdapter(adapter);
            adapter.startListening();

        }
        else {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

            FirebaseRecyclerOptions<Products> options =
                    new FirebaseRecyclerOptions.Builder<Products>()
                            .setQuery(reference.orderByChild("name").startAt(SearchInput), Products.class)
                            .build();

            FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                    new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {
                            productViewHolder.pName.setText(products.getName());
                            productViewHolder.pDesc.setText(products.getDesc());
                            productViewHolder.pPrice.setText("₹" + products.getPrice());
                            productViewHolder.bNow.setText("Edit");
                            productViewHolder.bNow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SearchProductsActivity.this, AdminEditProducts.class);
                                    intent.putExtra("data", products.getPid());
                                    startActivity(intent);
                                }
                            });

                            Picasso.get().load(products.getImg())
                                    .placeholder(R.drawable.imgplaceholder)
                                    .into(productViewHolder.pImage);

//                            productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(SearchProductsActivity.this, ProductDetailActivity.class);
//                                    intent.putExtra("pid", products.getPid());
//                                    startActivity(intent);
//                                }
//                            });
                        }

                        @NonNull
                        @Override
                        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                            ProductViewHolder holder = new ProductViewHolder(view);
                            return holder;
                        }
                    };
            searchList.setAdapter(adapter);
            adapter.startListening();
        }

    }

}