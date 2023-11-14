package com.example.nt118;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nt118.Class.ChangePasswordData;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

public class ChangePassword extends AppCompatActivity implements Server.PostResponseListener {
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Intent intent = getIntent();
        boolean noNeedPassword = intent.getBooleanExtra("noNeedPassword", false);
        String username = intent.getStringExtra("username");

        if (noNeedPassword){
            ((TextInputLayout)findViewById(R.id.til_old_password)).setVisibility(View.INVISIBLE);
            ((TextInputLayout)findViewById(R.id.til_old_password)).setEnabled(false);
        }

        ((Button)findViewById(R.id.btn_apply)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_password = ((TextInputLayout)findViewById(R.id.til_old_password)).getEditText().getText().toString().trim();
                String new_password = ((TextInputLayout)findViewById(R.id.til_new_password)).getEditText().getText().toString().trim();
                String renew_password = ((TextInputLayout)findViewById(R.id.til_renew_password)).getEditText().getText().toString().trim();

                if (noNeedPassword){
                    ChangePasswordData data = new ChangePasswordData();
                    data.setNewPassword(Server.hashPassword(new_password));
                    data.setUsername(username);
                    data.setOldPassword("");

                    String jsonString = new Gson().toJson(data);

                    Log.i("testlogin", jsonString);
                    progressDialog = new ProgressDialog(ChangePassword.this);
                    progressDialog.setMessage("Đang thực hiện POST...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    Server server = new Server();
                    server.postAsync("https://tester.cazo-dev.net/NT118/api/Home/ChangePassword", jsonString, ChangePassword.this);
                }
            }
        });
    }
    @Override
    public void onPostCompleted(String response){
        progressDialog.dismiss();
        if (response.equals("204")) {
            finish();
        } else Toast.makeText(this, "Response code: " + response, Toast.LENGTH_SHORT).show();
    }
}