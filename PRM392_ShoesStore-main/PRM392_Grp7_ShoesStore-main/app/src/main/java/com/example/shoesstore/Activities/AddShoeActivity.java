package com.example.shoesstore.Activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoesstore.Models.Shoe;
import com.example.shoesstore.R;
import com.example.shoesstore.Util.Database;
import com.example.shoesstore.Util.ShoeDBHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;


public class AddShoeActivity extends AppCompatActivity {
    Button btnAdd, btnBack;
    EditText txtShoeName, txtShoeDescription, txtShoePrimaryPrice, txtQuantity;
    Database database;
    ShoeDBHelper shoeDBHelper;
    ImageView imageView;
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;
    StorageReference storageReference;
    FirebaseStorage storage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_shoe);

        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnCancel);

        txtShoeName = findViewById(R.id.txtShoeName);
        txtShoeDescription = findViewById(R.id.txtShoeDescription);
        txtShoePrimaryPrice = findViewById(R.id.txtShoePrimaryPrice);
        txtQuantity = findViewById(R.id.txtQuatity);
        imageView = findViewById(R.id.imgView_Shoe);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        database = new Database(this, "ShoesStore.sqlite", null, 1);
        shoeDBHelper = new ShoeDBHelper(database);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddShoeActivity.this, AdminViewActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void selectImage(View view) {
// Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    public void AddAndUploadImage(View view) {
        if (txtShoeName.getText().toString().isEmpty()
                || txtShoeDescription.getText().toString().isEmpty()
                || txtShoePrimaryPrice.getText().toString().isEmpty()
                || txtQuantity.getText().toString().isEmpty()) {
            Toast.makeText(AddShoeActivity.this, "Please enter data in fields", Toast.LENGTH_SHORT).show();
        } else if (filePath != null) {
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            ContentResolver contentResolver = getContentResolver();
            String fileType = contentResolver.getType(filePath);
            String[] part = fileType.split("/");
            String subType = part[1];
            String fileName = UUID.randomUUID().toString();

            // Defining the child of storageReference
            StorageReference ref = storageReference.child("images/" + fileName);
            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Image uploaded successfully
                            // Dismiss dialog
                            progressDialog.dismiss();
                            Toast
                                    .makeText(AddShoeActivity.this,
                                            "Image Uploaded!!",
                                            Toast.LENGTH_SHORT)
                                    .show();
                            String shoeName = txtShoeName.getText().toString();
                            String description = txtShoeDescription.getText().toString();
                            double shoePrimaryPrice = Double.parseDouble(txtShoePrimaryPrice.getText().toString());
                            int quantity = Integer.parseInt(txtQuantity.getText().toString());

                            Shoe shoeNew = new Shoe(shoeName, (fileName + "." + subType), description, shoePrimaryPrice, quantity, false);
                            shoeDBHelper.AddNewShoe(shoeNew);
                            Toast.makeText(AddShoeActivity.this, "Add Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddShoeActivity.this,AdminViewActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(AddShoeActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }
}
