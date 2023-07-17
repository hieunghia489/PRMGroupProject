package com.example.shoesstore.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstore.Activities.CartActivity;
import com.example.shoesstore.Activities.DetailProductActivity;
import com.example.shoesstore.Activities.UserViewActivity;
import com.example.shoesstore.Models.Shoe;
import com.example.shoesstore.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class UserViewAdapter extends RecyclerView.Adapter<UserViewAdapter.ViewHolder> {
    Context context;
    ArrayList<Shoe> shoeArrayList;
    UserViewActivity userViewActivity;

    public UserViewAdapter(ArrayList<Shoe> shoeArrayList, UserViewActivity userViewActivity) {
        this.shoeArrayList = shoeArrayList;
        this.userViewActivity = userViewActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.user_view_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shoe shoe = shoeArrayList.get(position);
        holder.tvPrice.setText("" + shoe.getShoePrimaryPrice());
        holder.tvName.setText(shoe.getShoeName());
        String[] part = shoeArrayList.get(holder.getAdapterPosition()).getImgName().split("\\.");
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
                    holder.img.setImageBitmap(bitmap);
                }
            });
        }catch(Exception e){
            Log.d("Error", e.getMessage());
        }
    }

    public int getItemCount() {
        return shoeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvPrice, tvName;
        Button btnAddCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_product);
            tvPrice = itemView.findViewById(R.id.product_price);
            tvName = itemView.findViewById(R.id.product_name);
            btnAddCart = itemView.findViewById(R.id.btnAddCart);

            btnAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Shoe shoe = shoeArrayList.get(position);
                    userViewActivity.addCart(shoe.getShoeId(), shoe.getShoePrimaryPrice());
                    Intent intent = new Intent(userViewActivity, CartActivity.class);
                    userViewActivity.startActivity(intent);
                    userViewActivity.finish();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(userViewActivity, DetailProductActivity.class);
                    intent.putExtra("ShoeID", shoeArrayList.get(getAdapterPosition()).getShoeId());
                    userViewActivity.startActivity(intent);
                    userViewActivity.finish();
                }
            });
        }
    }
}
