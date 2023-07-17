package com.example.shoesstore.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstore.Models.BuyDetail;
import com.example.shoesstore.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class BuyDetailAdapter extends RecyclerView.Adapter<BuyDetailAdapter.ViewHolder> {
    ArrayList<BuyDetail> buyDetails;

    public BuyDetailAdapter(ArrayList<BuyDetail> buyDetails) {
        this.buyDetails = buyDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.buy_detail_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BuyDetail buyDetail = buyDetails.get(position);
        String[] part = buyDetail.getShoe().getImgName().split("\\.");
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
        holder.txtName.setText(buyDetail.getShoe().getShoeName());
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();

        // Add text with the desired style
        String text = buyDetail.getShoePriceWhenBuy() + "";
        int start = stringBuilder.length();
        stringBuilder.append(text);
        int end = stringBuilder.length();

        // Apply the StyleSpan to the text range
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        stringBuilder.setSpan(styleSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.newPrice.setText(buyDetail.getShoePriceWhenBuy() + "");
    }

    @Override
    public int getItemCount() {
        return buyDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtName, oldPrice, newPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgBuyDetail);
            txtName = itemView.findViewById(R.id.txtBuyDetailShoeName);
            oldPrice = itemView.findViewById(R.id.txtBuyDetailOldPrice);
            newPrice = itemView.findViewById(R.id.txtBuyDetailNewPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
