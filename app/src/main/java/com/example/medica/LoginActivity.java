package com.example.medica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medica.Model.Users;
import com.example.medica.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    EditText phone, password;
    CheckBox rememberme;
    Button login;
    TextView forget, admin, notAdmin;
    ProgressDialog loadingBar;
    String parentDatbase = "Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.phone_login);
        password = findViewById(R.id.pass_login);
        login = findViewById(R.id.login_login);
        forget = findViewById(R.id.forget);
        rememberme = findViewById(R.id.remember);
        loadingBar = new ProgressDialog(this);
        admin = findViewById(R.id.admin);
        notAdmin = findViewById(R.id.notadmin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        Paper.init(this);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setText("Login as Admin");
                admin.setVisibility(View.INVISIBLE);
                notAdmin.setVisibility(View.VISIBLE);
                parentDatbase = "Admins";
            }
        });

        notAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setText("LOGIN");
                notAdmin.setVisibility(View.INVISIBLE);
                admin.setVisibility(View.VISIBLE);
                parentDatbase = "Users";
            }
        });






    }

    private void LoginUser() {
        String mPhone = phone.getText().toString();
        String mPassword = password.getText().toString();

        if (TextUtils.isEmpty(mPassword) ||
                TextUtils.isEmpty(mPhone)){
            Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
        }else {

            loadingBar.setTitle("Loging In");
            loadingBar.setMessage("Please wait while we are checking the details");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccess(mPhone, mPassword);

        }
    }

    private void AllowAccess(final String mPhone, final String mPassword) {

        if (rememberme.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, mPhone);
            Paper.book().write(Prevalent.UserPasswordKey, mPassword);
            Paper.book().write(Prevalent.UserType, parentDatbase);
        }


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(parentDatbase).child(mPhone).exists()){
                    Users userData = snapshot.child(parentDatbase).child(mPhone).getValue(Users.class);



                    if (userData.getPhone().equals(mPhone)){

                        if (userData.getPassword().equals(mPassword)){



                           if (parentDatbase.equals("Admins")){

                               Toast.makeText(LoginActivity.this,"Signed in Successfully", Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();

                               Intent intent = new Intent(getApplicationContext(), AdminCategoryActivity.class);
                               startActivity(intent);
                               finish();

                           }

                           else if (parentDatbase.equals("Users")){
                               Toast.makeText(LoginActivity.this,"Signed in Successfully", Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();

                               Intent intent = new Intent(getApplicationContext(), ShopPage.class);
                               startActivity(intent);
                               finish();
                           }





                        }
                        else {
                            Toast.makeText(LoginActivity.this,"Incorrect Password", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }

                    }


                }else
                {
                    Toast.makeText(LoginActivity.this,"User doesn't exist, create a new account", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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