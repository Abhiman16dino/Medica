package com.example.medica;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medica.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    TextView closeTextButton, saveTextButton, profileChangeTextButton;
    EditText fullNameEditText, userPhoneEditText, addressEditText, pincodeEditText, emailEditText;
    CircleImageView profileImageView;
    private StorageTask uploadTask;
    private Uri imageUri;
    public String myUrl = "";
    StorageReference storageProfilePictureRef;
    String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        setHooks();


        userInfoDisplay(profileImageView, fullNameEditText, userPhoneEditText, addressEditText, pincodeEditText, emailEditText);

        closeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")){
                    userInfoSaved();

                }else {
                    updateOnlyUserInfo();
                }
            }
        });

        profileChangeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);

            }
        });

    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText addressEditText, final EditText pincodeEditText, final EditText emailEditText) {

      //  Toast.makeText(SettingsActivity.this , "Display function running - " + Prevalent.currentOnlineUser.getPhone() + "Phone", Toast.LENGTH_SHORT).show();

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    if (snapshot.child("Image").exists()){
                        String image = snapshot.child("Image").getValue().toString();
                        String name = snapshot.child("Name").getValue().toString();
                        String email = snapshot.child("Email").getValue().toString();


                            String phone = snapshot.child("Phone").getValue().toString();


                       // String password = snapshot.child("Password").getValue().toString();
                        String pincode = snapshot.child("Pincode").getValue().toString();
                        String address = snapshot.child("Address").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);
                       // passwordEditText.setText(password);
                        emailEditText.setText(email);
                        pincodeEditText.setText(pincode);
                        addressEditText.setText(address);
                    }
                    else{

                        String name = snapshot.child("Name").getValue().toString();
                        String email = snapshot.child("Email").getValue().toString();
                        String phone = snapshot.child("Phone").getValue().toString();
                        // String password = snapshot.child("Password").getValue().toString();
                        String pincode = snapshot.child("Pincode").getValue().toString();
                        String address = snapshot.child("Address").getValue().toString();

                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);
                        // passwordEditText.setText(password);
                        emailEditText.setText(email);
                        pincodeEditText.setText(pincode);
                        addressEditText.setText(address);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void updateOnlyUserInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("Name", fullNameEditText.getText().toString());
        userMap.put("Address", addressEditText.getText().toString());
        userMap.put("Phone", userPhoneEditText.getText().toString());
        userMap.put("Email", emailEditText.getText().toString());
        userMap.put("Pincode", pincodeEditText.getText().toString());
      //  userMap.put("Password", passwordEditText.getText().toString());

        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);




        startActivity(new Intent(SettingsActivity.this, ShopPage.class));
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profileImageView.setImageURI(imageUri);
        }else {
            Toast.makeText(SettingsActivity.this, "Error try again" , Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }

    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(fullNameEditText.getText().toString()) ||
                TextUtils.isEmpty(userPhoneEditText.getText().toString()) ||
                TextUtils.isEmpty(addressEditText.getText().toString()) ||
                TextUtils.isEmpty(pincodeEditText.getText().toString()) ||
              //  TextUtils.isEmpty(passwordEditText.getText().toString()) ||
                TextUtils.isEmpty(emailEditText.getText().toString())
        ){
            Toast.makeText(SettingsActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (checker.equals("clicked")){
            uploadImage();
        }

    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Profile");
        progressDialog.setMessage("Please wait, while we are updating your profile");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null){
            final  StorageReference fileRef = storageProfilePictureRef.child(Prevalent.currentOnlineUser.getPhone() + ".jpg");
            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }


                    return fileRef.getDownloadUrl();
                }
            })
            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("Name", fullNameEditText.getText().toString());
                        userMap.put("Address", addressEditText.getText().toString());
                        userMap.put("Phone", userPhoneEditText.getText().toString());
                        userMap.put("Email", emailEditText.getText().toString());
                        userMap.put("Pincode", pincodeEditText.getText().toString());
                     //   userMap.put("Password", passwordEditText.getText().toString());
                        userMap.put("Image", myUrl);
                        Prevalent.currentOnlineUser.setImage(myUrl);

                        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SettingsActivity.this, ShopPage.class));
                        finish();

                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            })
            ;

        }else {
            Toast.makeText(SettingsActivity.this, "Image not selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void setHooks() {
        profileImageView = findViewById(R.id.settings_profile_image);
        closeTextButton = findViewById(R.id.close_settings);
        saveTextButton = findViewById(R.id.update_settings);
        fullNameEditText = findViewById(R.id.settings_name);
        userPhoneEditText = findViewById(R.id.settings_phone);
        addressEditText = findViewById(R.id.settings_address);
        profileChangeTextButton = findViewById(R.id.changeProfileImage);
        pincodeEditText = findViewById(R.id.setting_pincode);
        // = findViewById(R.id.settings_password);
        emailEditText = findViewById(R.id.settings_email);
        profileChangeTextButton = findViewById(R.id.changeProfileImage);
    }



}