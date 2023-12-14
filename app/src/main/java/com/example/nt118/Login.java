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

import com.example.nt118.Class.AdminData;
import com.example.nt118.Class.NhanVien;
import com.example.nt118.Class.SharedPrefsHelper;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.Map;

public class Login extends AppCompatActivity {
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPrefsHelper helper = new SharedPrefsHelper(this);
        if (helper.getUsername() != null){
            ((CheckBox)findViewById(R.id.chb_saveLogin)).setChecked(true);
            if (helper.getUsername().contains("admin")) {
                AdminLogin(helper.getUsername(), helper.getPassword());
            } else
                StartLogin(helper.getUsername(),helper.getPassword());
        }
        ((Button)findViewById(R.id.btn_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((TextInputLayout)findViewById(R.id.til_username)).getEditText().getText().toString().trim();
                String password = ((TextInputLayout)findViewById(R.id.til_password)).getEditText().getText().toString().trim();

                if (username.contains("admin")){
                    AdminLogin(username,Server.hashPassword(password));
                    return;
                }

                StartLogin(username, Server.hashPassword(password));
            }
        });
    }
    private void AdminLogin(String username, String password){
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("ADMIN: Đang thực hiện POST...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Server server = new Server();
        AdminData data = new AdminData();
        data.setId(Integer.valueOf(username.replace("admin","")));
        data.setMK(password);

        String jsonString = new Gson().toJson(data);

        server.postAsync("https://s3.cazo-dev.net/NT118/api/Admin/login", jsonString, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(Map.Entry<String, Integer> response) {
                String status = "";
                if (response.getValue() == 200){
                    boolean result = Boolean.valueOf(response.getKey());
                    if (result){
                        SharedPrefsHelper helper = new SharedPrefsHelper(Login.this);
                        if (((CheckBox)findViewById(R.id.chb_saveLogin)).isChecked()){
                            helper.saveUsernameAndPassword(username, password);
                        } else  helper.clearData();

                        Intent intent = new Intent(Login.this, Admin.class);
                        intent.putExtra("id", data.getId());
                        intent.putExtra("mk", data.getMK());
                        startActivity(intent);
                        finish();
                        status = "Đăng nhập thành công!";
                    } else status = "Sai tên đăng nhập hoặc mật khẩu";
                } else
                if (response.getValue() == 204){
                    progressDialog.dismiss();
                    Intent intent = new Intent(Login.this, ChangePassword.class);
                    intent.putExtra("noNeedPassword", true);
                    intent.putExtra("admin", true);
                    intent.putExtra("username", String.valueOf(data.getId()));
                    startActivity(intent);
                    return;
                } else status = "Response code: " + response.getValue().toString();
                progressDialog.dismiss();
                Toast.makeText(Login.this, status, Toast.LENGTH_SHORT).show();
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

        server.postAsync("https://s3.cazo-dev.net/NT118/api/Home/Login", jsonString, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(Map.Entry<String, Integer> response) {
                boolean result;
                String status = "";
                if (response.getValue() == 200){
                    result = Boolean.valueOf(response.getKey());
                    if (result){
                        SharedPrefsHelper helper = new SharedPrefsHelper(Login.this);
                        if (((CheckBox)findViewById(R.id.chb_saveLogin)).isChecked()){
                            helper.saveUsernameAndPassword(nhanVien.getMANV(),nhanVien.getMK());
                        } else  helper.clearData();
                        server.postAsync("https://s3.cazo-dev.net/NT118/api/Home/CheckTRPH", new Gson().toJson(nhanVien.getMANV()), new Server.PostResponseListener() {
                            @Override
                            public void onPostCompleted(Map.Entry<String, Integer> response) {
                                progressDialog.dismiss();
                                if (response.getValue() == 204){
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    intent.putExtra("manv", nhanVien.getMANV());
                                    intent.putExtra("pass", nhanVien.getMK());
                                    startActivity(intent);
                                    finish();
                                } else if (response.getValue() == 200){
                                    Intent intent = new Intent(Login.this, SecondActivity.class);
                                    intent.putExtra("manv", nhanVien.getMANV());
                                    intent.putExtra("pass", nhanVien.getMK());
                                    intent.putExtra("phban", response.getKey());
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, "Respone code: " + response.getValue().toString(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                        //response = "Đăng nhập thành công!";
                    } else status = "Sai tên đăng nhập hoặc mật khẩu";
                } else
                if (response.getValue() == 204){
                    progressDialog.dismiss();
                    Intent intent = new Intent(Login.this, ChangePassword.class);
                    intent.putExtra("noNeedPassword", true);
                    intent.putExtra("username", nhanVien.getMANV());
                    startActivity(intent);
                    return;
                } else status = "Response code: " + response.getValue().toString();
                progressDialog.dismiss();
                Toast.makeText(Login.this, status, Toast.LENGTH_SHORT).show();
            }
        });
    }

}