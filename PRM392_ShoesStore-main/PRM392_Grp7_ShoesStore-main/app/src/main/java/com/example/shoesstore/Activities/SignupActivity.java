package com.example.shoesstore.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoesstore.Models.User;
import com.example.shoesstore.R;
import com.example.shoesstore.Util.Database;
import com.example.shoesstore.Util.UserDBHelper;

public class SignupActivity extends AppCompatActivity {
    EditText txtUsername, txtPassword, txtConfirm;
    Database database;
    UserDBHelper userDBHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        database = new Database(this, "ShoesStore.sqlite", null, 1);
        userDBHelper = new UserDBHelper(database);
        txtUsername = findViewById(R.id.txtSignupUsername);
        txtPassword = findViewById(R.id.txtSignupPassword);
        txtConfirm = findViewById(R.id.txtSignupConfirmPassword);
    }

    public void btnSignup_Click(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(SignupActivity.this);
        if (txtUsername.getText().toString().isEmpty() || txtPassword.getText().toString().isEmpty() || txtConfirm.getText().toString().isEmpty()) {
            dialog.setMessage("Please Input");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (!txtPassword.getText().toString().equals(txtConfirm.getText().toString())) {
            dialog.setMessage("Password is not same");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            if (userDBHelper.checkExist(txtUsername.getText().toString())) {
                dialog.setMessage("Username is exist");
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            } else {
                dialog.setMessage("Signup Success");
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userDBHelper.regisUser(txtUsername.getText().toString(), txtPassword.getText().toString());
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        }
    }
}
