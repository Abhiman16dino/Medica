package com.example.medica;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class VerifyPhone extends AppCompatActivity {

    private String verificationId;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private  EditText phoneNo;
    private  EditText otp;
    private  Button login;
    private  TextView getotp;
    private  TextView otpsent;
    private TextView resendOTP;
    private boolean getotpclicked = false;
    private TextView countdowntimer;
    Dialog dialog;
    FirebaseUser user;

    String mName, mEmail, mPassword, mPhone, mAddress, mPincode;

    String Data ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        mName = getIntent().getStringExtra("Name");
        mEmail = getIntent().getStringExtra("Email");
        mPassword = getIntent().getStringExtra("Password");
        mPhone = getIntent().getStringExtra("Phone");
        mAddress = getIntent().getStringExtra("Address");
        mPincode = getIntent().getStringExtra("Pincode");

        Data = getIntent().getStringExtra("Data").toString();

        Toast.makeText(VerifyPhone.this, mPhone, Toast.LENGTH_SHORT).show();




        initElements();


    }

    public void initElements(){

        mAuth = FirebaseAuth.getInstance();
        phoneNo = findViewById(R.id.txtPhone);
        otp = findViewById(R.id.txtotp);
        getotp = findViewById(R.id.get_otp);
        login  = findViewById(R.id.btnLogin);
        resendOTP = findViewById(R.id.resend_otp);
        otpsent = findViewById(R.id.otp_sent);
        countdowntimer = findViewById(R.id.countdown_timer);
        phoneNo.setText(mPhone);
        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getotpOnclick();

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginOnClick();;

            }
        });
        login.setTextColor(Color.parseColor("#C0BEBE"));



    }

    public void loginOnClick(){

        String number = phoneNo.getText().toString().trim();
        String otp1 = otp.getText().toString().trim();

        if (number.length() == 10 && otp1.length() > 4){

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            dialog = new Dialog(VerifyPhone.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_wait);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            verifyCode(otp1);


        }else {

            if (number.isEmpty() || number.length() < 10){

                phoneNo.setError("Valid number is required");
                phoneNo.requestFocus();

            }else if (otp1.isEmpty() || otp1 .length() < 5){

                otp.setError("Valid OTP is required");
                otp.requestFocus();


            }


        }

    }

    private void verifyCode(String code){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        FirebaseAuth.getInstance().getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        signInWithCredential(credential);


    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        mPhone = getIntent().getStringExtra("Phone");

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user !=null){

                                if (Data.equals("reset")){
                                    Intent intent = new Intent(VerifyPhone.this, ResetPassword.class);
                                    intent.putExtra("Phone", mPhone);
                                    startActivity(intent);
                                }
                               else {
                                    validatePhone(mPhone, mName, mEmail, mPassword, mAddress, mPincode);

                                    Intent i = new Intent(VerifyPhone.this,ShopPage.class);
                                    i.putExtra("Phone", mPhone);
                                    startActivity(i);
                                    finish();

                                }




                            }else {

                                if (dialog != null){

                                    dialog.dismiss();
                                    Toast.makeText(VerifyPhone.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                });
    }



    private void validatePhone(final String mPhone, final String mName, final String mEmail, final String mPassword, final String mAddress, final String mPincode) {

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(mPhone).exists())){
                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("Phone", mPhone);
                    userData.put("Name", mName);
                    userData.put("Email", mEmail);
                    userData.put("Password", mPassword);
                    userData.put("Address", mAddress);
                    userData.put("Pincode", mPincode);

                   rootRef.child("Users").child(mPhone).updateChildren(userData)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful()){
                                       Toast.makeText(VerifyPhone.this, "Account created" , Toast.LENGTH_LONG).show();

                                   }else {
                                       Toast.makeText(VerifyPhone.this, "Error " , Toast.LENGTH_LONG).show();
                                   }
                               }
                           });





                }else{
                    Toast.makeText(VerifyPhone.this, "This phone:"+ mPhone + " already exist", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(VerifyPhone.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getotpOnclick(){

        if (!getotpclicked){

            String num   = phoneNo.getText().toString().trim();

            if(num.length() != 10){

                phoneNo.setError("Valid number is required");
                phoneNo.requestFocus();

            }else {

                getotpclicked = true;
                String phoneNumber = "+91" + num;
                sendVerificationCode(phoneNumber);
                getotp.setTextColor(Color.parseColor("#C0BEBE"));
                dialog = new Dialog(VerifyPhone.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_wait);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


            }

        }


    }

    private void sendVerificationCode(String phoneNumber) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBack)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        @Override
        public void onCodeSent(@NonNull String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

            dialog.dismiss();
            login.setTextColor(Color.parseColor("#000000"));
            otpsent.setText("OTP has been sent yo your mobile number");
            otpsent.setVisibility(View.VISIBLE);
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;

            countdowntimer.setVisibility(View.VISIBLE);

            new CountDownTimer(60000,1000){


                @Override
                public void onTick(long millisUntilFinished) {

                    countdowntimer.setText("" + millisUntilFinished/1000);

                }

                @Override
                public void onFinish() {

                    resendOTP.setVisibility(View.VISIBLE);
                    countdowntimer.setVisibility(View.INVISIBLE);

                }
            }.start();
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null){

                otp.setText(code);
                verifyCode(code);


            }


        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            getotpclicked = false;
          //  getotp.setTextColor(Color.parseColor("0000FF"));
            Toast.makeText(VerifyPhone.this,e.getMessage(), Toast.LENGTH_LONG).show();
            dialog.dismiss();

        }
    };


}