package com.example.medica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medica.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    EditText nameEditText, phoneEditText, addressEditText, cityEditText;
    Button confirmOrderBtn;
    int totalAmount;
   public String allproducts1 = "Orders 1 - ", allpNames = "";
   String pid = "", subref;

   CardView preData;
   TextView preName, preAdd, prePhone;
    final DatabaseReference orderRefAdmin = FirebaseDatabase.getInstance().getReference().child("Admin Orders");

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        nameEditText = findViewById(R.id.shipment_name);
        phoneEditText = findViewById(R.id.shipment_phone);
        addressEditText = findViewById(R.id.shipment_address);
        cityEditText = findViewById(R.id.shipment_city);
        confirmOrderBtn = findViewById(R.id.confirm_final_order_btn);
        preData = findViewById(R.id.cview);
        preAdd = findViewById(R.id.preadd);
        preName = findViewById(R.id.prename);
        prePhone = findViewById(R.id.prephone);

         // totalAmount =(int) getIntent().getExtras().get("Total");
       // Toast.makeText(ConfirmFinalOrderActivity.this, total+"", Toast.LENGTH_SHORT).show();

        totalAmount = getIntent().getIntExtra("Total",0);
        pid = getIntent().getStringExtra("pid");

        Toast.makeText(ConfirmFinalOrderActivity.this, "tA = " + totalAmount + " pid = " + pid, Toast.LENGTH_LONG).show();

        getCurrentUserData();


        preData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameEditText.setText(preName.getText());
                addressEditText.setText(preAdd.getText());
                phoneEditText.setText(prePhone.getText());

            }
        });








            confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (totalAmount != 0){
                    check();
                    }
                    else
                    {
                        Toast.makeText(ConfirmFinalOrderActivity.this, "Checkout code", Toast.LENGTH_LONG).show();
                        checkOutcode(pid);
                    }
                }
            });








    }

    private void getCurrentUserData() {

//        final DatabaseReference rf = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
//        rf.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                preName.setText(snapshot.child("Name").getValue(String.class));
//                preAdd.setText(snapshot.child("Address").getValue(String.class));
//                prePhone.setText(snapshot.child("Phone").getValue(String.class));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        preName.setText(Prevalent.currentOnlineUser.getName());
        preAdd.setText(Prevalent.currentOnlineUser.getAddress());
        prePhone.setText(Prevalent.currentOnlineUser.getPhone());
    }

    private void checkOutcode (final String pid) {

        if (TextUtils.isEmpty(nameEditText.getText().toString())
                || TextUtils.isEmpty(phoneEditText.getText().toString())
                || TextUtils.isEmpty(addressEditText.getText().toString())
                || TextUtils.isEmpty(cityEditText.getText().toString())){
            Toast.makeText(ConfirmFinalOrderActivity.this, "All fieds are required", Toast.LENGTH_SHORT).show();
        }
        else {
            final String saveCurrentTime, saveCurrentDate;
            Calendar calforDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            saveCurrentDate = currentDate.format(calforDate.getTime());
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calforDate.getTime());



                    subref = saveCurrentDate.toString() + " " + saveCurrentTime.toString();

            int total = getIntent().getIntExtra("totalAmount1",0);
            String productName = getIntent().getStringExtra("name");

            String orderID = "MED" + productName.substring(0,3) + phoneEditText.getText().toString().substring(6) + saveCurrentTime
                    .replaceAll(":", "").replaceAll(" ", "");

            final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone()).child(subref);

            HashMap<String, Object> orderMap = new HashMap<>();
            orderMap.put("totalAmont", total);
            orderMap.put("name", nameEditText.getText().toString());
            orderMap.put("phone", phoneEditText.getText().toString());
            orderMap.put("date", saveCurrentDate);
            orderMap.put("time", saveCurrentTime);
            orderMap.put("address", addressEditText.getText().toString());
            orderMap.put("city",cityEditText.getText().toString());
            orderMap.put("state","order received");
            orderMap.put("pName",productName);
            orderMap.put("orderID", orderID);
           // orderMap.put("totalAmount",total);

            orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ConfirmFinalOrderActivity.this, "Order Placed " + allproducts1, Toast.LENGTH_LONG).show();
                        orderRef.child("Products").setValue(pid);
                        Intent intent = new Intent(ConfirmFinalOrderActivity.this, ShopPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }
                }
            });

            orderRefAdmin.child(subref).updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    orderRefAdmin.child(subref).child("Products").setValue(pid);
                }
            });



        }

    }

    private void check() {
        if (TextUtils.isEmpty(nameEditText.getText().toString()) 
                || TextUtils.isEmpty(phoneEditText.getText().toString())
                    || TextUtils.isEmpty(addressEditText.getText().toString())
                        || TextUtils.isEmpty(cityEditText.getText().toString())){
            Toast.makeText(ConfirmFinalOrderActivity.this, "All fieds are required", Toast.LENGTH_SHORT).show();
        }
        else {
            confirmOrder();
        }
    }

    private void confirmOrder() {
        final String saveCurrentTime, saveCurrentDate;
        Calendar calforDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calforDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calforDate.getTime());

       subref = saveCurrentDate.toString() + " " + saveCurrentTime.toString();



        final DatabaseReference products = FirebaseDatabase.getInstance().getReference().child("Cart List")
                .child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products");
        products.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allproducts1 = "";
                for (DataSnapshot s: snapshot.getChildren()){
                    allproducts1 = allproducts1 + " |Quantity = "+ s.child("quantity").getValue() +"| |pid = " + s.child("pid").getValue()+ "|";
                    //allProducts.append(s.child("pid").getValue(String.class));
                    Toast.makeText(ConfirmFinalOrderActivity.this, allproducts1, Toast.LENGTH_SHORT).show();
                }
                allpNames = "";
                for (DataSnapshot s: snapshot.getChildren()){
                    allpNames = allpNames + s.child("pName").getValue();
                    //allProducts.append(s.child("pid").getValue(String.class));
                    Toast.makeText(ConfirmFinalOrderActivity.this, allproducts1, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String orderID = "MED" + allpNames.substring(0,3) + phoneEditText.getText().toString().substring(6) + saveCurrentTime
                .replaceAll(":", "").replaceAll(" ", "");

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone()).child(subref);
        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("totalAmont", totalAmount);
        orderMap.put("name", nameEditText.getText().toString());
        orderMap.put("phone", phoneEditText.getText().toString());
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("address", addressEditText.getText().toString());
        orderMap.put("city",cityEditText.getText().toString());
        orderMap.put("state","order received");
        orderMap.put("orderID", orderID);
       // orderMap.put("Products", allproducts1);

        orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ConfirmFinalOrderActivity.this, "Order Placed " + allproducts1, Toast.LENGTH_LONG).show();
                    orderRef.child("Products").setValue(allproducts1);
                    orderRef.child("pName").setValue(allpNames);
                    Intent intent = new Intent(ConfirmFinalOrderActivity.this, ShopPage.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }
            }
        });

        orderRefAdmin.child(subref).updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                orderRefAdmin.child(subref).child("Products").setValue(pid);
            }
        });



    }
}