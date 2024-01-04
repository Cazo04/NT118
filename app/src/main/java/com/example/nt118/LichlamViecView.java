package com.example.nt118;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nt118.Adapter.LichLamViecViewAdapter;
import com.example.nt118.Class.LichLamViecData;
import com.example.nt118.Class.NhanVien;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class LichlamViecView extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LichLamViecViewAdapter adapter;
    private List<LichLamViecData> dataList;
    private List<LichLamViecData> dataFromServer;
    private NhanVien nhanvien;
    private Button buttonT2, buttonT3, buttonT4, buttonT5, buttonT6, buttonT7;
    private void getdata(){
        Server server = new Server();
        String json = new Gson().toJson(nhanvien);
        server.postAsync("https://s3.cazo-dev.net/NT118/api/NhanVien/LayDanhSachCongViecTheoTuan", json, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(Map.Entry<String, Integer> response) {
                if (response.getValue() == 200){
                    dataFromServer.clear();

                    for (LichLamViecData data : LichLamViecData.convertJsonToList(response.getKey())){
                        dataFromServer.add(data);

                    }
                } else errorResponse(response.getValue().toString() + " " + response.getKey());
            }
        });
    }
    private void errorResponse(String mess){
        Toast.makeText(this, "Response code: " + mess, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lichlam_viec_view);
        Intent intent = getIntent();
        nhanvien= (NhanVien) intent.getSerializableExtra("nhanVien");
        Log.i("texting", nhanvien.getMANV());
        recyclerView = findViewById(R.id.recycler_view);
        dataFromServer = new ArrayList<>();
        dataList = new ArrayList<>();
        adapter = new LichLamViecViewAdapter(dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        buttonT2 = findViewById(R.id.buttonT2);
        buttonT3 = findViewById(R.id.buttonT3);
        buttonT4 = findViewById(R.id.buttonT4);
        buttonT5 = findViewById(R.id.buttonT5);
        buttonT6 = findViewById(R.id.buttonT6);
        buttonT7 = findViewById(R.id.buttonT7);

        buttonT2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtons(buttonT2, 2);
            }

        });
        buttonT3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtons(buttonT3, 3);
            }

        });
        buttonT4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtons(buttonT4, 4);
            }

        });
        buttonT5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtons(buttonT5, 5);
            }

        });
        buttonT6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtons(buttonT6, 6);
            }

        });
        buttonT7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtons(buttonT7, 7);
            }

        });
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        int delta = Calendar.MONDAY - today;
        calendar.add(Calendar.DAY_OF_MONTH, delta);
        dayMonth = new ArrayList<>();
        for (int i = 2; i <= 7; i++) {
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            String buttonText = "T" + i + "\n" + dayOfMonth;
            switch (i) {
                case 2:
                    buttonT2.setText(buttonText);
                    break;
                case 3:
                    buttonT3.setText(buttonText);
                    break;
                case 4:
                    buttonT4.setText(buttonText);
                    break;
                case 5:
                    buttonT5.setText(buttonText);
                    break;
                case 6:
                    buttonT6.setText(buttonText);
                    break;
                case 7:
                    buttonT7.setText(buttonText);
                    break;
            }
            dayMonth.add(calendar.get(Calendar.DAY_OF_MONTH));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        getdata();
    }
    private List<Integer> dayMonth;
    int day = 0;
    private void updateButtons(Button clickedButton, int clickedDay) {
        Button[] buttons = {buttonT2, buttonT3, buttonT4, buttonT5, buttonT6, buttonT7};
        for (Button button : buttons) {
            button.setEnabled(button != clickedButton);
        }
        day = clickedDay;

        dataList.clear();
        for (LichLamViecData data : dataFromServer){
            if (dayMonth.get(day-2) == data.convertNgayBatDauToDate().getDate()){
                dataList.add(data);
            }
        }
        adapter.notifyDataSetChanged();
    }
}