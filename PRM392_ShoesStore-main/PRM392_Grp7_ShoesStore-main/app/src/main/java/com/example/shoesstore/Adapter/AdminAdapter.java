package com.example.shoesstore.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstore.Activities.AdminViewActivity;
import com.example.shoesstore.Models.Shoe;
import com.example.shoesstore.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> {
    ArrayList<Shoe> shoeArrayList;
    AdminViewActivity context;
    public AdminAdapter(AdminViewActivity context, ArrayList<Shoe> shoeArrayList){
        this.shoeArrayList = shoeArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.admin_view_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shoe shoe = shoeArrayList.get(holder.getAdapterPosition());

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
                    holder.imgViewAdmin.setImageBitmap(bitmap);
                }
            });
        }catch(Exception e){
            Log.d("Error", e.getMessage());
        }
        holder.nameShoe.setText(shoe.getShoeName());

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.DialogEdit(shoeArrayList.get(holder.getAdapterPosition()));
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.DialogDelete(shoeArrayList.get(holder.getAdapterPosition()).getShoeId(), shoeArrayList.get(holder.getAdapterPosition()).getShoeName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoeArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgViewAdmin, imgEdit, imgDelete;
        TextView nameShoe;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgViewAdmin = itemView.findViewById(R.id.imgViewAdmin);
            imgEdit = itemView.findViewById(R.id.imgViewEdit);
            imgDelete = itemView.findViewById(R.id.imgViewDelete);
            nameShoe = itemView.findViewById(R.id.nameShoe);
        }
    }
}
