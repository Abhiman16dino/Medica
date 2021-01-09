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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.medica.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminProductActivity extends AppCompatActivity {

    private  String CategoryName, productRandomKey;

    Button addNewProduct;
    private ProgressDialog loadingBar;
    EditText pName, pDesc, pPrice;
    ImageView pImage;
    private  static final int GalleryPick =1;
    private Uri imageUri;

    String Name, Desc, Price, saveCurrentDate, saveCurrentTime, downloadImageUrl;
    private StorageReference productImageRef;

    private DatabaseReference productRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        CategoryName = getIntent().getExtras().get("category").toString();
        productImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        loadingBar = new ProgressDialog(this);


        addNewProduct = findViewById(R.id.addNewProduct);
        pName = findViewById(R.id.productName);
        pDesc = findViewById(R.id.productDesc);
        pImage = findViewById(R.id.productImage);
        pPrice = findViewById(R.id.productPrice);

        pImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });


        addNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ValidateProduct();
            }
        });



    }

    private void ValidateProduct() {


        Desc = pDesc.getText().toString();
        Name = pName.getText().toString();
        Price = pPrice.getText().toString();

        if (imageUri == null){
            Toast.makeText(AdminProductActivity.this, "Product Image is Required", Toast.LENGTH_SHORT).show();

        }
        else  if (TextUtils.isEmpty(Desc) || TextUtils.isEmpty(Name) || TextUtils.isEmpty(Price)){
            Toast.makeText(AdminProductActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
        }
        else {

            StoreProductInfo();

        }


    }

    private void StoreProductInfo() {

        loadingBar.setTitle("Saving Product...");
        loadingBar.setMessage("Please wait while we are adding this product to our database");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();



        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, YYYY");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());


        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = productImageRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String mes = e.getMessage() + "";
                Toast.makeText(AdminProductActivity.this, "Error: " + mes, Toast.LENGTH_LONG).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminProductActivity.this, "Product Image Upload Success", Toast.LENGTH_LONG).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){

                            throw task.getException();

                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return  filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){

                            
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminProductActivity.this, "Getting Product Image url : SUCCESS", Toast.LENGTH_LONG).show();

                            SaveProductInfoToDatabase();


                        }
                    }
                });
            }


        });


    }

    private void SaveProductInfoToDatabase() {



        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("desc", Desc);
        productMap.put("img", downloadImageUrl);
        productMap.put("name", Name);
        productMap.put("category", CategoryName);
        productMap.put("price", Price);

        productRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    loadingBar.dismiss();

                    Toast.makeText(AdminProductActivity.this, "Product is Live", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AdminProductActivity.this, AdminCategoryActivity.class);
                    startActivity(intent);


                }
                else {
                    loadingBar.dismiss();
                    Toast.makeText(AdminProductActivity.this, "Error Saving :" + task.getException() + "", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void OpenGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GalleryPick );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick  && resultCode == RESULT_OK && data!=null){

            imageUri = data.getData();
            pImage.setImageURI(imageUri);



        }
    }


}