package com.example.medica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText name, phone, email, address, pincode, password, cpassword;
    Button register;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);
        register = findViewById(R.id.register);
        loadingBar = new ProgressDialog(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });











    }

    private void createAccount() {

        String mName, mEmail, mPassword, mPhone, mAddress, mPincode, mCPassword;

        mName = name.getText().toString();
        mEmail = email.getText().toString();
        mPassword = password.getText().toString();
        mCPassword = cpassword.getText().toString();
        mAddress = address.getText().toString();
        mPincode = pincode.getText().toString();
        mPhone = phone.getText().toString();

        EmailValid(mEmail);

        if (TextUtils.isEmpty(mName) ||
                TextUtils.isEmpty(mEmail) ||
                TextUtils.isEmpty(mPassword) ||
                TextUtils.isEmpty(mCPassword) ||
                TextUtils.isEmpty(mAddress) ||
                TextUtils.isEmpty(mPincode) || TextUtils.isEmpty(mPhone)){
            Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
        }
        else if (!EmailValid(mEmail)){

            Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
        }

        else if(!mPassword.equals(mCPassword)){

            Toast.makeText(getApplicationContext(), "Password not matched", Toast.LENGTH_SHORT).show();

        }
        else if(mPincode.length() != 6){
            Toast.makeText(getApplicationContext(), "Invalid Pincode", Toast.LENGTH_SHORT).show();
        }

        else {

            loadingBar.setTitle("Creating Account");
            loadingBar.setMessage("Please wait while we are checking the details");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();



            validatePhone(mPhone, mName, mEmail, mPassword, mAddress, mPincode);

        }




    }

    private static boolean EmailValid(String mEmail) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (mEmail == null)
            return false;
        return pat.matcher(mEmail).matches();
    }

    private void validatePhone(final String mPhone, final String mName, final String mEmail, final String mPassword, final String mAddress, final String mPincode) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(mPhone).exists())){

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("Name", mName);
                    userdataMap.put("Email", mEmail);
                    userdataMap.put("Password", mPassword);
                    userdataMap.put("Address", mAddress);
                    userdataMap.put("Pincode", mPincode);
                    userdataMap.put("Phone", mPhone);

                    RootRef.child("Users").child(mPhone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Your Account is created", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, ShopPage.class);
                                        startActivity(intent);
                                        finish();



                                    }else {
                                        Toast.makeText(getApplicationContext(), "Unknown error: Please try again later", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });




                }else {

                    Toast.makeText(getApplicationContext(), "This "+mPhone+" already exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}