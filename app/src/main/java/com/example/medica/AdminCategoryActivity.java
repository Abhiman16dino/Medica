package com.example.medica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView preventionGear, fitness, medicine, mask, glove, otherGear, suppliments, equipments, clothings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

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
}