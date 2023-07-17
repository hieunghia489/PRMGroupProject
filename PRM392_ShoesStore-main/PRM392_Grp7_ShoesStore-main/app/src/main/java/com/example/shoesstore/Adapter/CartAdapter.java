package com.example.shoesstore.Adapter;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstore.Activities.CartActivity;
import com.example.shoesstore.Models.CartDetail;
import com.example.shoesstore.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    ArrayList<CartDetail> cartDetails;
    CartActivity cartActivity;

    public CartAdapter(ArrayList<CartDetail> cartDetails, CartActivity cartActivity) {
        this.cartDetails = cartDetails;
        this.cartActivity = cartActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cart_detail_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartDetail cartDetail = cartDetails.get(position);
        String[] part = cartDetail.getShoe().getImgName().split("\\.");
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
        holder.txtName.setText(cartDetail.getShoe().getShoeName());
        holder.txtQuantity.setText("Số lượng: " + cartDetail.getQuantity());
        holder.txtPrice.setText("Giá tiền :" + (cartDetail.getShoe().getShoePrimaryPrice() * cartDetail.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return cartDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtName, txtQuantity, txtPrice;
        Button btnXoa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgCartDetail);
            txtName = itemView.findViewById(R.id.txtCartDetailShoeName);
            txtQuantity = itemView.findViewById(R.id.txtCartDetailQuantity);
            txtPrice = itemView.findViewById(R.id.txtCardDetailPrice);
            btnXoa = itemView.findViewById(R.id.btnDeleteCartDetail);

            btnXoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    CartDetail cartDetail = cartDetails.get(position);
                    cartActivity.removeCart(cartDetail.getShoeId());
                }
            });
        }
    }
}
