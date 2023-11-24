package com.example.nt118;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Admin extends AppCompatActivity {

    private TextView tvTime;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("EEEE\ndd/MM/yyyy\nHH:mm", new Locale("vi", "VN"));
    private final Handler timeHandler = new Handler();
    private Runnable timeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String mk = intent.getStringExtra("mk");

        tvTime = findViewById(R.id.tvTime);
        setupTimeUpdater();

        ((Button)findViewById(R.id.btnManageDepartments)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, AdminManageDepartments.class);
                intent.putExtra("id", id);
                intent.putExtra("mk", mk);
                startActivity(intent);
            }
        });
    }

    private void setupTimeUpdater() {
        timeRunnable = new Runnable() {
            @Override
            public void run() {
                tvTime.setText(timeFormat.format(new Date()));
                timeHandler.postDelayed(this, 1000);
            }
        };
        timeHandler.post(timeRunnable);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeHandler.removeCallbacks(timeRunnable);
    }
}