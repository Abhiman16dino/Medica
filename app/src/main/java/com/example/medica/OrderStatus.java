package com.example.medica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderStatus extends AppCompatActivity {

    TextView orderID, currentStatus;
    RadioButton verified, shipped, onTheWay, packageReceived;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        orderID = findViewById(R.id.status_orderid);
        currentStatus = findViewById(R.id.currentstatus);
        verified = findViewById(R.id.radioButton_verified);
        shipped = findViewById(R.id.radioButton2_shipped);
        onTheWay = findViewById(R.id.radioButton3_ontheway);
        packageReceived = findViewById(R.id.radioButton4_received);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);

        String oID = getIntent().getStringExtra("orderidreal");
        String usrPh = getIntent().getStringExtra("phno");

        Toast.makeText(OrderStatus.this, oID, Toast.LENGTH_SHORT).show();

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Admin Orders").child(oID);
        final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Orders").child(usrPh).child(oID);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentStatus.setText(snapshot.child("state").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = radioGroup.getCheckedRadioButtonId();
                switch (id){
                    case R.id.radioButton_verified:
                        Toast.makeText(OrderStatus.this, "Verified", Toast.LENGTH_SHORT).show();
                        ref.child("state").setValue("verified");
                        ref1.child("state").setValue("verified");
                        break;
                    case R.id.radioButton2_shipped:
                        Toast.makeText(OrderStatus.this, "Shipped", Toast.LENGTH_SHORT).show();
                        ref.child("state").setValue("shipped");
                        ref1.child("state").setValue("shipped");
                        break;
                    case R.id.radioButton3_ontheway:
                        Toast.makeText(OrderStatus.this, "On the way", Toast.LENGTH_SHORT).show();
                        ref.child("state").setValue("on the way");
                        ref1.child("state").setValue("on the way");
                        break;
                    case R.id.radioButton4_received:
                        Toast.makeText(OrderStatus.this, "Received", Toast.LENGTH_SHORT).show();
                        ref.child("state").setValue("received");
                        ref1.child("state").setValue("received");
                        break;
                }
            }
        });






    }

}