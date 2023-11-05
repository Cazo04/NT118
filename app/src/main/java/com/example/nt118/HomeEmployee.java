package com.example.nt118;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class HomeEmployee extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CardView myCardView = findViewById(R.id.lich_lam_viec_ke_tiep);
        myCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Phương thức onClick xử lý sự kiện khi CardView được nhấn
            }
        });
}
