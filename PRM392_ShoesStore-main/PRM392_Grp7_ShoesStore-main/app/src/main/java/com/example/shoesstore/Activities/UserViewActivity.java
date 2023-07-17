package com.example.shoesstore.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shoesstore.Adapter.UserViewAdapter;
import com.example.shoesstore.Models.Shoe;
import com.example.shoesstore.R;
import com.example.shoesstore.Util.BuyDetailDBHelper;
import com.example.shoesstore.Util.CartDBHelper;
import com.example.shoesstore.Util.CartDetailDBHelper;
import com.example.shoesstore.Util.Database;
import com.example.shoesstore.Util.ShoeDBHelper;
import com.example.shoesstore.Util.ShoeDetailDBHelper;

import java.util.ArrayList;

public class UserViewActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Shoe> list;
    GridLayoutManager layoutManager;
    UserViewAdapter userViewAdapter;
    ShoeDBHelper shoeDBHelper;
    ShoeDetailDBHelper shoeDetailDBHelper;
    CartDBHelper cartDBHelper;
    CartDetailDBHelper cartDetailDBHelper;
    SharedPreferences sharedPreferences;
    Database database;
    int userId = 0;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        database = new Database(this, "ShoesStore.sqlite", null, 1);
        shoeDBHelper = new ShoeDBHelper(database);
        shoeDetailDBHelper = new ShoeDetailDBHelper(database);
        cartDBHelper = new CartDBHelper(database);
        cartDetailDBHelper = new CartDetailDBHelper(database);

        toolbar = findViewById(R.id.userViewToolBar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("ShoesStore", Context.MODE_PRIVATE);
        checkAuthentication();
        list = new ArrayList<>();

        recyclerView = findViewById(R.id.recycle_user_view_list);

        userViewAdapter = new UserViewAdapter(list, UserViewActivity.this);
        recyclerView.setAdapter(userViewAdapter);

//        layoutManager = new GridLayoutManager(this,2, LinearLayoutManager.VERTICAL, false);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        setData();
    }

    private void checkAuthentication() {
        userId = sharedPreferences.getInt("userId", 0);

        if (userId == 0) {
            Intent intent = new Intent(UserViewActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setData() {
        shoeDBHelper.AddTable();
        Cursor cursor = shoeDBHelper.GetAllShoe();
        while (cursor.moveToNext()) {
            int shoeId = cursor.getInt(0);
            String shoeName = cursor.getString(1);
            String imgName = cursor.getString(2);
            String shoeDescription = cursor.getString(3);
            double shoePrimaryPrice = cursor.getDouble(4);
            int quantity = cursor.getInt(5);
            boolean isDelete = cursor.getInt(6) == 1;
            list.add(new Shoe(shoeId, shoeName, imgName, shoeDescription, shoePrimaryPrice, quantity, isDelete));
        }
        cursor.close();
        userViewAdapter.notifyDataSetChanged();
    }

    public void addCart(int shoeId, double price) {
        int cartID = cartDBHelper.AddCart(userId);
        cartDetailDBHelper.AddCart(cartID, shoeId, price);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mUserExit) {
            finishAffinity();
        }
        if (item.getItemId() == R.id.mUserCart) {
            Intent intent = new Intent(UserViewActivity.this, CartActivity.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.mUserBuyHistory) {
            Intent intent = new Intent(UserViewActivity.this, BuyHistoryActivity.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.mUserDashboard) {
            Intent intent = new Intent(UserViewActivity.this, UserViewActivity.class);
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
            Intent intent = new Intent(UserViewActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void SearchShoeName(View view){
        EditText txtSearch = findViewById(R.id.etSearch);
        String search = txtSearch.getText().toString();

        if(search.isEmpty() || search.equals("")){
            Toast.makeText(this, "Not Found Shoe!!!", Toast.LENGTH_SHORT).show();
            list.clear();
            setData();
        }
        else{
            list.clear(); // Xóa dữ liệu hiện tại trong danh sách
            Cursor cursor = shoeDBHelper.GetShoeByShoeName(search); // Lấy dữ liệu mới từ cơ sở dữ liệu
            while (cursor.moveToNext()) {
                int shoeId = cursor.getInt(0);
                String shoeName = cursor.getString(1);
                String imgName = cursor.getString(2);
                String shoeDescription = cursor.getString(3);
                double shoePrimaryPrice = cursor.getDouble(4);
                int quantity = cursor.getInt(5);
                boolean isDelete = cursor.getInt(6) == 1;
                list.add(new Shoe(shoeId, shoeName, imgName, shoeDescription, shoePrimaryPrice, quantity, isDelete));
            }
            cursor.close();
            userViewAdapter.notifyDataSetChanged();
        }
    }
}