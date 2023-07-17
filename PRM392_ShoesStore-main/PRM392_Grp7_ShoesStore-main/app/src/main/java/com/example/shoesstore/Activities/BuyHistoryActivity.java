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

import com.example.shoesstore.Adapter.BuyHistoryAdapter;
import com.example.shoesstore.Models.BuyHistory;
import com.example.shoesstore.R;
import com.example.shoesstore.Util.BuyHistoryDBHelper;
import com.example.shoesstore.Util.Database;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BuyHistoryActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    RecyclerView rcy;
    ArrayList<BuyHistory> buyHistories;
    BuyHistoryAdapter adapter;
    BuyHistoryDBHelper buyHistoryDBHelper;
    Database database;
    int userId = 0;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_history);

        database = new Database(this, "ShoesStore.sqlite", null, 1);
        buyHistoryDBHelper = new BuyHistoryDBHelper(database);

        toolbar = findViewById(R.id.userBuyHistoryToolbar);
        setSupportActionBar(toolbar);
//        database.QueryData("Delete From BuyHistory");
        sharedPreferences = getSharedPreferences("ShoesStore", Context.MODE_PRIVATE);
        checkAuthentication();

        buyHistories = new ArrayList<>();
        rcy = findViewById(R.id.rcyViewBuyHistory);
        adapter = new BuyHistoryAdapter(buyHistories, BuyHistoryActivity.this);
        setData();
        rcy.setAdapter(adapter);
        rcy.setLayoutManager(new LinearLayoutManager(this));
    }

    private void checkAuthentication() {
        userId = sharedPreferences.getInt("userId", 0);

        if (userId == 0) {
            Intent intent = new Intent(BuyHistoryActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setData() {
        Cursor cursor = buyHistoryDBHelper.GetBuyHistoryByUserId(userId);
        while (cursor.moveToNext()) {
            int buyId = cursor.getInt(0);
            String dateInString = cursor.getString(1);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;
            try {
                date = dateFormat.parse(dateInString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int userId = cursor.getInt(2);
            double price = cursor.getDouble(3);
            boolean isDelete = cursor.getInt(4) == 1;
            buyHistories.add(new BuyHistory(buyId, date, userId, price, isDelete));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
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
            Intent intent = new Intent(BuyHistoryActivity.this, CartActivity.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.mUserBuyHistory) {
            Intent intent = new Intent(BuyHistoryActivity.this, BuyHistoryActivity.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.mUserDashboard) {
            Intent intent = new Intent(BuyHistoryActivity.this, UserViewActivity.class);
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
            Intent intent = new Intent(BuyHistoryActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
