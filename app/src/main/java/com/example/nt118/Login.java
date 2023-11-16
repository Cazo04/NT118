package com.example.nt118;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.nt118.Class.NhanVien;
import com.example.nt118.Class.SharedPrefsHelper;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

public class Login extends AppCompatActivity {
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPrefsHelper helper = new SharedPrefsHelper(this);
        if (helper.getUsername() != null){
            Log.i("testSave",helper.getUsername());
            Log.i("testSave",helper.getPassword());
            StartLogin(helper.getUsername(),helper.getPassword());
        }
        ((Button)findViewById(R.id.btn_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((TextInputLayout)findViewById(R.id.til_username)).getEditText().getText().toString().trim();
                String password = ((TextInputLayout)findViewById(R.id.til_password)).getEditText().getText().toString().trim();

                StartLogin(username, Server.hashPassword(password));
            }
        });
    }
    private void StartLogin(String username, String password){
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Đang thực hiện POST...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Server server = new Server();
        //Log.i("TestLogin", password);
        NhanVien nhanVien = new NhanVien();
        nhanVien.setMANV(username);
        nhanVien.setMK(password);

        String jsonString = new Gson().toJson(nhanVien);

        server.postAsync("https://tester.cazo-dev.net/NT118/api/Home/Login", jsonString, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(String response) {
                boolean result;
                if (response.equals("true") || response.equals("false")){
                    result = Boolean.valueOf(response);
                    if (result){
                        server.postAsync("https://tester.cazo-dev.net/NT118/api/Home/CheckTRPH", new Gson().toJson(nhanVien.getMANV()), new Server.PostResponseListener() {
                            @Override
                            public void onPostCompleted(String response) {
                                progressDialog.dismiss();
                                Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                if (response.equals("204")){
                                    SharedPrefsHelper helper = new SharedPrefsHelper(Login.this);
                                    if (((CheckBox)findViewById(R.id.chb_saveLogin)).isChecked()){
                                        helper.saveUsernameAndPassword(nhanVien.getMANV(),nhanVien.getMK());
                                    } else  helper.clearData();

                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    intent.putExtra("manv", nhanVien.getMANV());
                                    intent.putExtra("pass", nhanVien.getMK());
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
                    progressDialog.dismiss();
                    Intent intent = new Intent(Login.this, ChangePassword.class);
                    intent.putExtra("noNeedPassword", true);
                    intent.putExtra("username", nhanVien.getMANV());
                    startActivity(intent);
                    return;
                } else response = "Response code: " + response;
                progressDialog.dismiss();
                Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
            }
        });
    }

}