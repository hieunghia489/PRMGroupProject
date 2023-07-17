package com.example.shoesstore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstore.Activities.BuyDetailActivity;
import com.example.shoesstore.Activities.BuyHistoryActivity;
import com.example.shoesstore.Models.BuyHistory;
import com.example.shoesstore.R;

import java.util.ArrayList;

public class BuyHistoryAdapter extends RecyclerView.Adapter<BuyHistoryAdapter.ViewHolder> {
    ArrayList<BuyHistory> buyHistories;
    BuyHistoryActivity buyHistoryActivity;

    public BuyHistoryAdapter(ArrayList<BuyHistory> buyHistories, BuyHistoryActivity buyHistoryActivity) {
        this.buyHistories = buyHistories;
        this.buyHistoryActivity = buyHistoryActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.buy_history_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BuyHistory buyHistory = buyHistories.get(position);

        holder.txtBuyHistoryDate.setText("Ngày Mua: " + buyHistory.getBuyDate().toString());
        holder.txtBuyHistoryId.setText("Mã Hóa Đơn: " + buyHistory.getBuyId());
        holder.txtBuyHistoryTotalPrice.setText("Tổng: " + buyHistory.getTotalPrice() + "");

    }

    @Override
    public int getItemCount() {
        return buyHistories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtBuyHistoryId, txtBuyHistoryDate, txtBuyHistoryTotalPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBuyHistoryDate = itemView.findViewById(R.id.txtBuyHistoryDate);
            txtBuyHistoryId = itemView.findViewById(R.id.txtBuyHistoryId);
            txtBuyHistoryTotalPrice = itemView.findViewById(R.id.txtBuyHistoryTotalPrice);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(buyHistoryActivity, BuyDetailActivity.class);
                    intent.putExtra("buyId",buyHistories.get(getAdapterPosition()).getBuyId());
                    buyHistoryActivity.startActivity(intent);
                    buyHistoryActivity.finish();
                }
            });
        }
    }
}
