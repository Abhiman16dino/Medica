package com.example.medica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.medica.Model.Users;
import com.example.medica.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button login, signup;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);
        Paper.init(this);
        loadingBar = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
        String db = Paper.book().read(Prevalent.UserType);

        if (UserPhoneKey != "" && UserPasswordKey != "" && db != ""){
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey) && !TextUtils.isEmpty(db)){
                AllowAccess(UserPhoneKey, UserPasswordKey, db);

                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }



    }

    private void AllowAccess(final String mPhone, final String mPassword, final  String db) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(db).child(mPhone).exists()){
                    Users userData = snapshot.child(db).child(mPhone).getValue(Users.class);



                    if (userData.getPhone().equals(mPhone)){

                        if (userData.getPassword().equals(mPassword)){

                            if (db.equals("Admins")){

                                Toast.makeText(MainActivity.this,"Signed in Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(getApplicationContext(), AdminCategoryActivity.class);
                                startActivity(intent);
                                finish();

                            }

                            else if (db.equals("Users")){
                                Toast.makeText(MainActivity.this,"Signed in Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(getApplicationContext(), ShopPage.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                        else {
                            Toast.makeText(MainActivity.this,"Incorrect Password", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }

                    }


                }else
                {
                    Toast.makeText(MainActivity.this,"User doesn't exist, create a new account", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}