package com.example.shoesstore.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.shoesstore.Adapter.CartAdapter;
import com.example.shoesstore.Models.Cart;
import com.example.shoesstore.Models.CartDetail;
import com.example.shoesstore.Models.Shoe;
import com.example.shoesstore.Models.ShoeDetail;
import com.example.shoesstore.R;
import com.example.shoesstore.Util.BuyDetailDBHelper;
import com.example.shoesstore.Util.BuyHistoryDBHelper;
import com.example.shoesstore.Util.CartDBHelper;
import com.example.shoesstore.Util.CartDetailDBHelper;
import com.example.shoesstore.Util.Database;
import com.example.shoesstore.Util.ShoeDBHelper;
import com.example.shoesstore.Util.ShoeDetailDBHelper;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    ArrayList<CartDetail> details;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    Database database;
    CartDBHelper cartDBHelper;
    CartDetailDBHelper cartDetailDBHelper;
    BuyHistoryDBHelper buyHistoryDBHelper;
    BuyDetailDBHelper buyDetailDBHelper;
    ShoeDetailDBHelper shoeDetailDBHelper;
    ShoeDBHelper shoeDBHelper;
    CartAdapter adapter;
    int userId = 0;
    double totalPrice;
    TextView txtTotalPrice;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        database = new Database(this, "ShoesStore.sqlite", null, 1);
        cartDBHelper = new CartDBHelper(database);
        cartDetailDBHelper = new CartDetailDBHelper(database);
        buyDetailDBHelper = new BuyDetailDBHelper(database);
        buyHistoryDBHelper = new BuyHistoryDBHelper(database);
        shoeDetailDBHelper = new ShoeDetailDBHelper(database);
        shoeDBHelper = new ShoeDBHelper(database);
        setUpDB();

        toolbar = findViewById(R.id.userCartToolbar);
        setSupportActionBar(toolbar);

        details = new ArrayList<>();

        sharedPreferences = getSharedPreferences("ShoesStore", Context.MODE_PRIVATE);
        checkAuthentication();

        recyclerView = findViewById(R.id.rcyViewCart);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);

        getAllCartDetail();
    }

    private void setUpDB() {
        cartDBHelper.AddTable();
        cartDetailDBHelper.AddTable();
        buyDetailDBHelper.AddTable();
        buyHistoryDBHelper.AddTable();
        shoeDetailDBHelper.AddTable();
    }

    private void checkAuthentication() {
        userId = sharedPreferences.getInt("userId", 0);

        if (userId == 0) {
            Intent intent = new Intent(CartActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void getAllCartDetail() {
        details.clear();
        int cartId = -1;
        Cursor cursor = cartDBHelper.GetDatabaseByUserId(userId);
        while (cursor.moveToNext()) {
            cartId = cursor.getInt(0);
        }
        cursor.close();
        if (cartId != -1) {
            double total = 0;
            cursor = cartDetailDBHelper.GetAllCartDetail(cartId);
            while (cursor.moveToNext()) {
                int cartDetailId = cursor.getInt(0);
                int shoeId = cursor.getInt(2);
                int quantity = cursor.getInt(3);
                double totalPrice = cursor.getDouble(4);
                Shoe Shoe = shoeDBHelper.getShoeById(shoeId);
                Cart Cart = cartDBHelper.getCartById(cartId);
                details.add(new CartDetail(cartDetailId, cartId, shoeId, quantity, totalPrice, Shoe, Cart));
                total += totalPrice * quantity;
            }
            cursor.close();
            totalPrice = total;
            txtTotalPrice.setText("" + total);
            adapter = new CartAdapter(details, CartActivity.this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    public void btnCartBuy_Click(View view) {
        if (details.size() > 0) {
            int buyId = buyHistoryDBHelper.AddBuy(userId, totalPrice);
            for (CartDetail detail : details) {
                for (int i = 0; i < detail.getQuantity(); i++) {
                    int shoeDetailId = shoeDetailDBHelper.getUnSoldShoe(detail.getShoeId());
                    double price = detail.getTotalPrice() / detail.getQuantity();
                    buyDetailDBHelper.CreateBuyDetail(buyId, detail.getShoeId(), shoeDetailId, price);
                    shoeDetailDBHelper.setSoldShoe(shoeDetailId);
                }
            }
            Intent intent = new Intent(CartActivity.this,BuyHistoryActivity.class);
            startActivity(intent);
            finish();
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
            Intent intent = new Intent(CartActivity.this, CartActivity.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.mUserBuyHistory) {
            Intent intent = new Intent(CartActivity.this, BuyHistoryActivity.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.mUserDashboard) {
            Intent intent = new Intent(CartActivity.this, UserViewActivity.class);
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
            Intent intent = new Intent(CartActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void removeCart(int shoeId){
        Cursor cursor = cartDBHelper.GetDatabaseByUserId(userId);
        if(cursor.moveToFirst()){
            cartDetailDBHelper.RemoveCart(cursor.getInt(0),shoeId);
        }
        getAllCartDetail();
    }
}