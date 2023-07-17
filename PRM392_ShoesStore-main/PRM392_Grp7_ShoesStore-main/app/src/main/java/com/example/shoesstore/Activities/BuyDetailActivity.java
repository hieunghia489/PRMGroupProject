package com.example.shoesstore.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstore.Adapter.BuyDetailAdapter;
import com.example.shoesstore.Models.BuyDetail;
import com.example.shoesstore.Models.Cart;
import com.example.shoesstore.Models.Shoe;
import com.example.shoesstore.R;
import com.example.shoesstore.Util.BuyDetailDBHelper;
import com.example.shoesstore.Util.CartDBHelper;
import com.example.shoesstore.Util.Database;
import com.example.shoesstore.Util.ShoeDBHelper;

import java.util.ArrayList;

public class BuyDetailActivity extends AppCompatActivity {
    ArrayList<BuyDetail> buyDetails;
    RecyclerView rcyView;
    Database database;
    int userId = 0;
    int buyId = 0;
    SharedPreferences sharedPreferences;
    BuyDetailDBHelper buyDetailDBHelper;
    BuyDetailAdapter adapter;
    ShoeDBHelper shoeDBHelper;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_detail);

        database = new Database(this, "ShoesStore.sqlite", null, 1);
        buyDetailDBHelper = new BuyDetailDBHelper(database);
        shoeDBHelper = new ShoeDBHelper(database);

        toolbar = findViewById(R.id.userBuyDetailToolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("ShoesStore", Context.MODE_PRIVATE);
        checkAuthentication();

        if (getIntent() != null) {
            buyId = getIntent().getIntExtra("buyId", 0);
        }

        buyDetails = new ArrayList<>();

        rcyView = findViewById(R.id.rcyViewBuyDetail);

        adapter = new BuyDetailAdapter(buyDetails);
        rcyView.setAdapter(adapter);
        rcyView.setLayoutManager(new LinearLayoutManager(this));
        setData();
    }

    private void checkAuthentication() {
        userId = sharedPreferences.getInt("userId", 0);

        if (userId == 0) {
            Intent intent = new Intent(BuyDetailActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setData() {
        if (buyId != 0) {
            Cursor cursor = buyDetailDBHelper.getBuyDetailByBuyId(buyId);
            while (cursor.moveToNext()) {
                int buyDetailId = cursor.getInt(0);
                int buyId = cursor.getInt(1);
                int shoeId = cursor.getInt(2);
                int shoeDetailId = cursor.getInt(3);
                double shoePriceWhenBuy = cursor.getDouble(4);
                boolean isDelete = cursor.getInt(5) == 1;
                Shoe shoe = shoeDBHelper.getShoeByBuyId(shoeId);
                buyDetails.add(new BuyDetail(buyDetailId, buyId, shoeId, shoeDetailId, shoePriceWhenBuy, isDelete,shoe));
            }
            cursor.close();
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.user_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mUserExit) {
            finishAffinity();
        }
        if (item.getItemId() == R.id.mUserCart) {
            Intent intent = new Intent(BuyDetailActivity.this, CartActivity.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.mUserBuyHistory) {
            Intent intent = new Intent(BuyDetailActivity.this, BuyHistoryActivity.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.mUserDashboard) {
            Intent intent = new Intent(BuyDetailActivity.this, UserViewActivity.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.mUserLogout) {
            SharedPreferences shared = getSharedPreferences("ShoesStore", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("userId");
            editor.remove("username");
            editor.remove("password");
            editor.remove("role");
            editor.apply();
            Intent intent = new Intent(BuyDetailActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}