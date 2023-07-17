package com.example.shoesstore.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoesstore.Models.User;
import com.example.shoesstore.R;
import com.example.shoesstore.Util.Database;
import com.example.shoesstore.Util.UserDBHelper;

public class LoginActivity extends AppCompatActivity {
    EditText txtUsername, txtPassword;
    SharedPreferences sharedPreferences;
    Database database;
    UserDBHelper userDBHelper;
    AlertDialog.Builder dialog;
    String username, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        database = new Database(this, "ShoesStore.sqlite", null, 1);
        dialog = new AlertDialog.Builder(this);

        userDBHelper = new UserDBHelper(database);
        userDBHelper.AddTable();

//        userDBHelper.deleteData();
        userDBHelper.addAdmin();

        txtPassword = findViewById(R.id.txtLoginPassword);
        txtUsername = findViewById(R.id.txtLoginEmail_Username);

        sharedPreferences = getSharedPreferences("ShoesStore", Context.MODE_PRIVATE);

        username = sharedPreferences.getString("username", "");
        password = sharedPreferences.getString("password", "");

        if (!username.isEmpty() || !password.isEmpty()) {
            User user = userDBHelper.checkLogin(username, password);
            if (user != null) {
                changeView(user);
            }
        }
    }

    public void btnLogin_Click(View view) {
        User user = userDBHelper.checkLogin(txtUsername.getText().toString(), txtPassword.getText().toString());
        if (user != null) {
            changeView(user);
        } else {
            loginFail();
        }
    }

    public void txtSignup_Click(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
        finish();
    }

    private void changeView(User user) {
        dialog.setMessage("Login Success");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (username.isEmpty() || password.isEmpty()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("userId", user.getUserId());
                    editor.putString("username", user.getUsername());
                    editor.putString("password", user.getPassword());
                    editor.putInt("role", user.getRole());
                    editor.apply();
                }

                if (user.getRole() == 2) {
                    Intent intent = new Intent(LoginActivity.this, UserViewActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(LoginActivity.this, AdminViewActivity.class);
                    startActivity(intent);
                    finish();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void loginFail() {
        dialog.setMessage("Login Error");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
