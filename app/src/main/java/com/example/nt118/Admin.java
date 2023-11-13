package com.example.nt118;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
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

        tvTime = findViewById(R.id.tvTime);
        setupTimeUpdater();
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