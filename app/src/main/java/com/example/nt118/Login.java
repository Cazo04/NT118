package com.example.nt118;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

public class Login extends AppCompatActivity implements Server.PostResponseListener {
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((Button)findViewById(R.id.btn_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((TextInputLayout)findViewById(R.id.til_username)).getEditText().getText().toString().trim();
                String password = ((TextInputLayout)findViewById(R.id.til_password)).getEditText().getText().toString().trim();

                progressDialog = new ProgressDialog(Login.this);
                progressDialog.setMessage("Đang thực hiện POST...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                Server server = new Server();
                password = server.hashPassword(password);
                //Log.i("TestLogin", password);
                NhanVien nhanVien = new NhanVien();
                nhanVien.setMANV(username);
                nhanVien.setMK(password);

                String jsonString = new Gson().toJson(nhanVien);

                server.postAsync("https://tester.cazo-dev.net/NT118/api/Home/Login", jsonString, Login.this);
            }
        });
    }
    @Override
    public void onPostCompleted(String response) {
        progressDialog.dismiss();
        boolean result;
        if (response.equals("true") || response.equals("false")){
            result = Boolean.valueOf(response);
            if (result){
                response = "Đăng nhập thành công!";
            } else response = "Sai tên đăng nhập hoặc mật khẩu";
        } else
        if (response.equals("204")){
            Intent intent = new Intent(this, ChangePassword.class);
            intent.putExtra("noNeedPassword", true);
            intent.putExtra("username", ((TextInputLayout)findViewById(R.id.til_username)).getEditText().getText().toString().trim());
            startActivity(intent);
            return;
        } else response = "Response code: " + response;

        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
    }
}