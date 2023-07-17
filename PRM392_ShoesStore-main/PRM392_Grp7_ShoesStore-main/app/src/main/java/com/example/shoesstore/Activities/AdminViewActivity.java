package com.example.shoesstore.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstore.Adapter.AdminAdapter;
import com.example.shoesstore.Models.Shoe;
import com.example.shoesstore.R;
import com.example.shoesstore.Util.Database;
import com.example.shoesstore.Util.ShoeDBHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AdminViewActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Shoe> shoeArrayList;
    Database database;
    ShoeDBHelper shoeDBHelper;
    AdminAdapter adapter;

    Toolbar toolbar;
    SharedPreferences sharedPreferences;
    int userId = 0;
    private Uri filePath;
    StorageReference storageReference;
    FirebaseStorage storage;
    private final int PICK_IMAGE_REQUEST = 22;
    ImageView imageViewEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);
        toolbar = findViewById(R.id.adminViewToolBar);
        setSupportActionBar(toolbar);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        database = new Database(this, "ShoesStore.sqlite", null, 1);
        sharedPreferences = getSharedPreferences("ShoesStore", Context.MODE_PRIVATE);
        checkAuthentication();
        shoeDBHelper = new ShoeDBHelper(database);
        shoeDBHelper.AddTable();
        recyclerView = findViewById(R.id.recyleView_admin_view);
        shoeArrayList = new ArrayList<>();
        GetDataShoe();
        adapter = new AdminAdapter(this, shoeArrayList);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void checkAuthentication() {
        userId = sharedPreferences.getInt("userId", 0);

        if (userId == 0) {
            Intent intent = new Intent(AdminViewActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void RefreshData() {
        shoeArrayList.clear();
        GetDataShoe();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mAdminAdd) {
//            DialogAdd();

            Intent intent = new Intent(AdminViewActivity.this, AddShoeActivity.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.mAdminManage) {
            Intent intent = new Intent(AdminViewActivity.this, AdminViewActivity.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.mAdminExit) {
            finishAffinity();
        }
        if (item.getItemId() == R.id.mAdminLogout) {
            SharedPreferences shared = getSharedPreferences("ShoesStore", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("userId");
            editor.remove("username");
            editor.remove("password");
            editor.remove("role");
            editor.apply();
            Intent intent = new Intent(AdminViewActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void DialogEdit(Shoe shoe) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.edit_shoe);

        Button btnEdit, btnCancel;
        EditText txtShoeName, txtShoeDescription, txtShoePrimaryPrice, txtQuantity;

        btnEdit = dialog.findViewById(R.id.btnEdit);
        btnCancel = dialog.findViewById(R.id.btnCancel);



        txtShoeName = dialog.findViewById(R.id.txtShoeName);
        imageViewEdit = dialog.findViewById(R.id.imgView_EditShoe);
        txtShoeDescription = dialog.findViewById(R.id.txtShoeDescription);
        txtShoePrimaryPrice = dialog.findViewById(R.id.txtShoePrimaryPrice);
        txtQuantity = dialog.findViewById(R.id.txtQuatity);

        txtShoeName.setText(shoe.getShoeName());

        String[] part = shoe.getImgName().split("\\.");
        String imageName = part[0];
        String subType = part[1];
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("images/" + imageName);
        try {
            File locaFile = File.createTempFile("tempfile", "." + subType);
            storageRef.getFile(locaFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(locaFile.getAbsolutePath());
                        imageViewEdit.setImageBitmap(bitmap);
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
        txtShoeDescription.setText(shoe.getShoeDescription());
        txtShoePrimaryPrice.setText("" + shoe.getShoePrimaryPrice());
        txtQuantity.setText("" + shoe.getQuantity());
        int shoeID = shoe.getShoeId();


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageRef.delete();
                if (filePath != null) {
                    // Code for showing progressDialog while uploading

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
                                    try {
                                        if (txtShoeName.getText().toString().isEmpty()
                                                || fileName.isEmpty()
                                                || txtShoeDescription.getText().toString().isEmpty()
                                                || txtShoePrimaryPrice.getText().toString().isEmpty()
                                                || txtQuantity.getText().toString().isEmpty()) {
                                            Toast.makeText(AdminViewActivity.this, "Please enter data in fields", Toast.LENGTH_SHORT).show();
                                        } else {
                                            int sID = shoeID;
                                            String shoeName = txtShoeName.getText().toString();
                                            String description = txtShoeDescription.getText().toString();
                                            double shoePrimaryPrice = Double.parseDouble(txtShoePrimaryPrice.getText().toString());
                                            int quantity = Integer.parseInt(txtQuantity.getText().toString());

                                            Shoe shoeEdit = new Shoe(sID, shoeName, (fileName + "." + subType), description, shoePrimaryPrice, quantity, false);
                                            shoeDBHelper.UpdateShoe(shoeEdit);
                                            Toast.makeText(AdminViewActivity.this, "Update Successfully", Toast.LENGTH_SHORT).show();

                                            dialog.dismiss();
                                            RefreshData();
                                        }
                                    } catch (Exception e) {
                                        Log.d("Error", e.getMessage());
                                    }

                                }
                            })

                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    // Error, Image not uploaded

                                    Toast
                                            .makeText(AdminViewActivity.this,
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
                                        }
                                    });
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showToast(String message) {
        Toast.makeText(AdminViewActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void DialogDelete(int shoeID, String shoeName) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this);
        dialogDelete.setMessage("Are you sure delete " + shoeName + " ?");
        dialogDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shoeDBHelper.DeleteShoe(shoeID);
                RefreshData();
            }
        });

        dialogDelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogDelete.show();
    }

    private void GetDataShoe() {
        Cursor dataShoe = shoeDBHelper.GetAllShoe();

        while (dataShoe.moveToNext()) {
            int booleanValue = dataShoe.getInt(6);
            boolean booleanFied = booleanValue != 0;
            shoeArrayList.add(new Shoe(dataShoe.getInt(0), dataShoe.getString(1), dataShoe.getString(2)
                    , dataShoe.getString(3), dataShoe.getDouble(4), dataShoe.getInt(5), booleanFied));
        }
    }

    public void selectImageEdit(View view) {
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
                imageViewEdit.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

}
