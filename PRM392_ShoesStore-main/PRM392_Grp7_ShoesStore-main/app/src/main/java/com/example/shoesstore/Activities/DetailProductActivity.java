package com.example.shoesstore.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoesstore.Adapter.BuyDetailAdapter;
import com.example.shoesstore.Models.Shoe;
import com.example.shoesstore.R;
import com.example.shoesstore.Util.BuyDetailDBHelper;
import com.example.shoesstore.Util.Database;
import com.example.shoesstore.Util.ShoeDBHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class DetailProductActivity extends AppCompatActivity {
    Database database;
    int shoeID = 0;
    ShoeDBHelper shoeDBHelper;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        database = new Database(this, "ShoesStore.sqlite", null, 1);
        shoeDBHelper = new ShoeDBHelper(database);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        if(getIntent() != null){
            shoeID = getIntent().getIntExtra("ShoeID", 0);
        }

        ImageView imageView = (ImageView) findViewById(R.id.detailImage);
        TextView tvName = (TextView) findViewById(R.id.detailShoeName);
        TextView tvDescription = (TextView) findViewById(R.id.detailDescription);
        Button btnBack = (Button) findViewById(R.id.detailbtnBack);

        Shoe shoe = shoeDBHelper.getShoeById(shoeID);

        String[] part = shoe.getImgName().split("\\.");
        String imageName = part[0];
        String subType = part[1];
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("images/" + imageName);
        try{
            File locaFile = File.createTempFile("tempfile","."+ subType);
            storageRef.getFile(locaFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(locaFile.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
            });
        }catch(Exception e){
            Log.d("Error", e.getMessage());
        }

        tvName.setText(shoe.getShoeName());
        tvDescription.setText(shoe.getShoeDescription());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailProductActivity.this, UserViewActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
