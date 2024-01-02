package com.example.nt118;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.nt118.Adapter.LichLamViecViewAdapter;
import com.example.nt118.Class.LichLamViecData;
import com.example.nt118.Class.NhanVien;

import java.util.ArrayList;
import java.util.List;

public class LichlamViecView extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LichLamViecViewAdapter adapter;
private NhanVien nhanvien;
    private Button buttonT2, buttonT3, buttonT4, buttonT5, buttonT6, buttonT7;
    private void getdata(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lichlam_viec_view);
        Intent intent = getIntent();
        nhanvien= (NhanVien) intent.getSerializableExtra("nhanVien");
        Log.i("texting", nhanvien.getMANV());
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new LichLamViecViewAdapter(new ArrayList<>());  // Khởi tạo adapter với danh sách trống
        recyclerView.setAdapter(adapter);

        buttonT2 = findViewById(R.id.buttonT2);
        buttonT3 = findViewById(R.id.buttonT3);
        buttonT4 = findViewById(R.id.buttonT4);
        buttonT5 = findViewById(R.id.buttonT5);
        buttonT6 = findViewById(R.id.buttonT6);
        buttonT7 = findViewById(R.id.buttonT7);

// Xử lý sự kiện click cho các button
        buttonT2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu lịch làm việc cho thứ 2 và hiển thị trong RecyclerView
            }

        }); buttonT3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu lịch làm việc cho thứ 2 và hiển thị trong RecyclerView
            }

        });
        buttonT4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu lịch làm việc cho thứ 2 và hiển thị trong RecyclerView
            }

        });
        buttonT5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu lịch làm việc cho thứ 2 và hiển thị trong RecyclerView
            }

        });
        buttonT6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu lịch làm việc cho thứ 2 và hiển thị trong RecyclerView
            }

        });
        buttonT7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu lịch làm việc cho thứ 2 và hiển thị trong RecyclerView
            }

        });
    }}