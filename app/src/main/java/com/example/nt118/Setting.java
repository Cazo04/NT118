package com.example.nt118;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.nt118.Class.SharedPrefsHelper;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ((Button)findViewById(R.id.btn_signout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefsHelper helper = new SharedPrefsHelper(Setting.this);
                helper.clearData();

                Intent intent = new Intent(Setting.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);

                finish();
            }
        });
    }
}