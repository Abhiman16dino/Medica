package com.example.medica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResetPassword extends AppCompatActivity {

TextView userPhone;
EditText usrNewPass;
Button set;

String mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        userPhone = findViewById(R.id.userRPhone);
        usrNewPass = findViewById(R.id.userNewPass);
        set = findViewById(R.id.UserRSet);

        mPhone = getIntent().getStringExtra("Phone");

        userPhone.setText(mPhone);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newPass = usrNewPass.getText().toString();

                if (TextUtils.isEmpty(newPass)){
                    Toast.makeText(ResetPassword.this,"Password Can't be null", Toast.LENGTH_SHORT).show();
                }
                else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(mPhone).child("Password");
                    reference.setValue(usrNewPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ResetPassword.this,"Password Changed, Please login again", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ResetPassword.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(ResetPassword.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });
    }
}