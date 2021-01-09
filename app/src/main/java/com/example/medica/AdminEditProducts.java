package com.example.medica;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AdminEditProducts extends AppCompatActivity {

    String pid, downloadImageUrl;
    ImageView productImageEdit;
    EditText productNameEdit, productDescEdit, productPriceEdit;
    Button savechanges;
    TextView deleteProduct;

    private  static final int GalleryPick =1;
    private Uri imageUri;
    private StorageReference productImageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_products);

        pid = getIntent().getStringExtra("data");
        Toast.makeText(AdminEditProducts.this, pid, Toast.LENGTH_SHORT).show();

        productImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");

        productImageEdit = findViewById(R.id.productImageEdit);
        productNameEdit = findViewById(R.id.productNameEdit);
        productDescEdit = findViewById(R.id.productDescEdit);
        productPriceEdit = findViewById(R.id.productPriceEdit);
        savechanges = findViewById(R.id.savechanges);
        deleteProduct = findViewById(R.id.deleteproduct);

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Products");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productNameEdit.setText(snapshot.child(pid).child("name").getValue(String.class));
                productDescEdit.setText(snapshot.child(pid).child("desc").getValue(String.class));
                productPriceEdit.setText(snapshot.child(pid).child("price").getValue(String.class));

                Picasso.get().load(snapshot.child(pid).child("img").getValue(String.class))
                        .placeholder(R.drawable.imgplaceholder)
                        .into(productImageEdit);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nName, nDesc, nPrice;

                nName = productNameEdit.getText().toString();
                nDesc = productDescEdit.getText().toString();
                nPrice = productPriceEdit.getText().toString();

                ref.child(pid).child("name").setValue(nName);
                ref.child(pid).child("desc").setValue(nDesc);
                ref.child(pid).child("price").setValue(nPrice);


                final StorageReference filePath = productImageRef.child(imageUri.getLastPathSegment() + pid + ".jpg");

                final UploadTask uploadTask = filePath.putFile(imageUri);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String mes = e.getMessage() + "";
                        Toast.makeText(AdminEditProducts.this, "Error: " + mes, Toast.LENGTH_LONG).show();
//                        loadingBar.dismiss();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(AdminEditProducts.this, "Product Image Upload Success", Toast.LENGTH_LONG).show();
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

                                    Toast.makeText(AdminEditProducts.this, "Getting Product Image url : SUCCESS", Toast.LENGTH_LONG).show();

                                    ref.child(pid).child("img").setValue(downloadImageUrl);

                                }
                            }
                        });
                    }


                });
            }
        });

        productImageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child(pid).removeValue();
                Intent intent = new Intent(AdminEditProducts.this, AdminAllProducts.class);
                startActivity(intent);
                finish();
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
            productImageEdit.setImageURI(imageUri);



        }
    }

}