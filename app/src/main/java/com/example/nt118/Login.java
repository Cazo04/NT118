package com.example.nt118;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nt118.Class.NhanVien;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

public class Login extends AppCompatActivity {
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

                server.postAsync("https://tester.cazo-dev.net/NT118/api/Home/Login", jsonString, new Server.PostResponseListener() {
                    @Override
                    public void onPostCompleted(String response) {
                        progressDialog.dismiss();
                        boolean result;
                        if (response.equals("true") || response.equals("false")){
                            result = Boolean.valueOf(response);
                            if (result){
                                server.postAsync("https://tester.cazo-dev.net/NT118/api/Home/CheckTRPH", new Gson().toJson(nhanVien.getMANV()), new Server.PostResponseListener() {
                                    @Override
                                    public void onPostCompleted(String response) {
                                        Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                        if (response.equals("204")){
                                            Intent intent = new Intent(Login.this, HomeEmployee.class);
                                            intent.putExtra("info", jsonString);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                                return;
                                //response = "Đăng nhập thành công!";
                            } else response = "Sai tên đăng nhập hoặc mật khẩu";
                        } else
                        if (response.equals("204")){
                            Intent intent = new Intent(Login.this, ChangePassword.class);
                            intent.putExtra("noNeedPassword", true);
                            intent.putExtra("username", nhanVien.getMANV());
                            startActivity(intent);
                            return;
                        } else response = "Response code: " + response;

                        Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}