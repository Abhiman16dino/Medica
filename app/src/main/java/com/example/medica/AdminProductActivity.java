package com.example.medica;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class AdminProductActivity extends AppCompatActivity {

    private  String CategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        CategoryName = getIntent().getExtras().get("category").toString();

        Toast.makeText(AdminProductActivity.this, CategoryName, Toast.LENGTH_LONG).show();
    }
}