package com.example.medica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.medica.Model.Products;
import com.example.medica.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class ShopPage extends AppCompatActivity {

DatabaseReference productRef;
private RecyclerView recyclerView;
RecyclerView.LayoutManager layoutManager;
FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;
    ProgressDialog loadingBar;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_page);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        loadingBar = new ProgressDialog(this);

        recyclerView = findViewById(R.id.rView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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

                productViewHolder.bNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopPage.this, ConfirmFinalOrderActivity.class);
                        intent.putExtra("pid", " |Quantity = "+1+"| |pid = "+products.getPid() + "|");
                        intent.putExtra("totalAmount1", Integer.parseInt(products.getPrice()));
                        intent.putExtra("name", products.getName());
                        startActivity(intent);
                    }
                });

                //here i set clicklistner on each cardview

                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopPage.this, ProductDetailActivity.class);
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
        recyclerView.setAdapter(adapter);

        adapter.startListening();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

       // menu.getItem(0).setIcon(R.drawable.glass);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.profile :
                Toast.makeText(getApplicationContext(), "Profile clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cart :
                Toast.makeText(getApplicationContext(), "Cart clicked", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(ShopPage.this, CartActivity.class);
                startActivity(intent2);
                break;
            case  R.id.logout:

                loadingBar.setTitle("Logging you out");
                loadingBar.setMessage("Please wait....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                Paper.book().destroy();
                Intent intent = new Intent(ShopPage.this, MainActivity.class);
                startActivity(intent);
                finish();

                loadingBar.dismiss();

                break;
            case R.id.setting:
                Intent intent1 = new Intent(ShopPage.this, SettingsActivity.class);
                startActivity(intent1);
                break;
            case R.id.orders:
                Toast.makeText(getApplicationContext(), "Orders clicked", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(ShopPage.this, OrderAc.class);
                startActivity(intent3);
                break;
            case R.id.categories:
                Toast.makeText(getApplicationContext(), "Categories clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ref:
                adapter.notifyDataSetChanged();
                break;
            case R.id.search:
                Intent intent4 = new Intent(ShopPage.this, SearchProductsActivity.class);
                intent4.putExtra("Admin","No");
                startActivity(intent4);
                Animatoo.animateZoom(ShopPage.this);
                break;

                default:
                    throw new IllegalStateException("Unexpected value: " + id);
        }
        return  true;
    }


}