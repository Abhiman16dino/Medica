package com.example.medica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import io.paperdb.Paper;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView preventionGear, fitness, medicine, mask, glove, otherGear, suppliments, equipments, clothings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        preventionGear = findViewById(R.id.prevention_gear);
        fitness = findViewById(R.id.fitness);
        medicine = findViewById(R.id. medicine);
        mask = findViewById(R.id.mask);
        glove = findViewById(R.id.glove);
        otherGear = findViewById(R.id.others_gear);
        suppliments = findViewById(R.id.supplements);
        equipments = findViewById(R.id.Equipments);
        clothings = findViewById(R.id.clothings);



        preventionGear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AdminProductActivity.class);
                intent.putExtra("category", "PrventionGear");
                startActivity(intent);
            }
        });

        fitness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AdminProductActivity.class);
                intent.putExtra("category", "Fitness");
                startActivity(intent);
            }
        });

        medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AdminProductActivity.class);
                intent.putExtra("category", "Medicine");
                startActivity(intent);
            }
        });

        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AdminProductActivity.class);
                intent.putExtra("category", "Mask");
                startActivity(intent);
            }
        });

        glove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AdminProductActivity.class);
                intent.putExtra("category", "Glove");
                startActivity(intent);
            }
        });

        otherGear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AdminProductActivity.class);
                intent.putExtra("category", "OtherGear");
                startActivity(intent);
            }
        });

        suppliments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AdminProductActivity.class);
                intent.putExtra("category", "Suppliment");
                startActivity(intent);
            }
        });

        equipments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AdminProductActivity.class);
                intent.putExtra("category", "Equipment");
                startActivity(intent);
            }
        });

        clothings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AdminProductActivity.class);
                intent.putExtra("category", "Clothing");
                startActivity(intent);
            }
        });





    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        menu.findItem(R.id.ref).setVisible(false);
        menu.findItem(R.id.categories).setVisible(false);
        menu.findItem(R.id.search).setTitle("All Products");      //Search id will now considered as all products in this activity
        menu.findItem(R.id.cart).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.profile :
                Toast.makeText(getApplicationContext(), "Profile clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.search :    //All Products

                Toast.makeText(getApplicationContext(), "all products clicked", Toast.LENGTH_SHORT).show();
                Intent intentx = new Intent(AdminCategoryActivity.this, AdminAllProducts.class);
                startActivity(intentx);

                break;
            case  R.id.logout:
                Paper.book().destroy();
                Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.orders:
//                Toast.makeText(getApplicationContext(), "Admin order clicked", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(AdminCategoryActivity.this, AdminAllOrders.class);
                startActivity(intent1);
                break;




        }
        return  true;
    }
}