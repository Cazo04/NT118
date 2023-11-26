package com.example.nt118;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nt118.Adapter.NhanVienInPhongBanAdapter;
import com.example.nt118.Adapter.TrphArrayAdapter;
import com.example.nt118.Class.AdminData;
import com.example.nt118.Class.EntryWrapper;
import com.example.nt118.Class.NhanVien;
import com.example.nt118.Class.PhongBan;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminManageDepartmentsMoreDetails extends AppCompatActivity {
    private AdminData adminData;
    private TrphArrayAdapter adapter;
    private NhanVienInPhongBanAdapter nhanVienInPhongBanAdapter;
    private PhongBan phongBan;
    private List<NhanVien> nhanViensInPHBAN;
    private List<NhanVien> nhanViensOutPHBAN;
    private String trph;
    private Server server;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_departments_more_details);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String mk = intent.getStringExtra("mk");
        adminData = new AdminData(id,mk);
        String maph = intent.getStringExtra("maph");
        phongBan = new PhongBan(maph);
        trph = intent.getStringExtra("trph");
        server = new Server();
        ((TextView)findViewById(R.id.txt_maph)).setText("MAPH: " + maph);
        nhanViensInPHBAN = new ArrayList<>();
        nhanViensOutPHBAN = new ArrayList<>();

        AutoCompleteTextView auto_complete_spinner_manv = findViewById(R.id.auto_complete_spinner_manv);
        adapter = new TrphArrayAdapter(this, nhanViensOutPHBAN);
        auto_complete_spinner_manv.setAdapter(adapter);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        nhanVienInPhongBanAdapter = new NhanVienInPhongBanAdapter(this, nhanViensInPHBAN);
        recyclerView.setAdapter(nhanVienInPhongBanAdapter);

        updateData();

        ((Button)findViewById(R.id.btn_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String manv = auto_complete_spinner_manv.getText().toString().trim();
                PhongBan pban = new PhongBan(phongBan.getMAPH());
                pban.setTRPH(manv);

                String json = new Gson().toJson(new EntryWrapper(new AbstractMap.SimpleEntry(adminData, pban)));
                server = new Server();
                server.postAsync("https://tester.cazo-dev.net/NT118/api/Admin/InsertNhanVienToPhongBan", json, new Server.PostResponseListener() {
                    @Override
                    public void onPostCompleted(Map.Entry<String, Integer> response) {
                        if (response.getValue() == 200){
                            Toast.makeText(AdminManageDepartmentsMoreDetails.this, "Đã thêm nhân viên", Toast.LENGTH_SHORT).show();
                            updateData();
                        } else Toast.makeText(AdminManageDepartmentsMoreDetails.this, "Response code: " + response.getValue().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        nhanVienInPhongBanAdapter.setOnNhanVienInPhongBanClickListener(new NhanVienInPhongBanAdapter.OnNhanVienInPhongBanClickListener() {
            @Override
            public void onNhanVienInPhongBanClick(int position) {

            }

            @Override
            public void onNhanVienInPhongBanLongClick(int position) {
                NhanVien nhanVien = nhanViensInPHBAN.get(position);

                PhongBan pban = new PhongBan(phongBan.getMAPH());
                pban.setTRPH(nhanVien.getMANV());

                String json = new Gson().toJson(new EntryWrapper(new AbstractMap.SimpleEntry(adminData, pban)));
                server = new Server();
                server.postAsync("https://tester.cazo-dev.net/NT118/api/Admin/DeleteNhanVienFromPhongBan", json, new Server.PostResponseListener() {
                    @Override
                    public void onPostCompleted(Map.Entry<String, Integer> response) {
                        if (response.getValue() == 200){
                            Toast.makeText(AdminManageDepartmentsMoreDetails.this, "Đã xóa nhân viên", Toast.LENGTH_SHORT).show();
                            updateData();
                        } else Toast.makeText(AdminManageDepartmentsMoreDetails.this, "Response code: " + response.getValue().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void updateData(){
        NhanVien nhanVien = new NhanVien();
        NhanVien.Checker checker = new NhanVien.Checker();
        checker.setMANV(true);
        checker.setHOTEN(true);
        checker.setPHBAN(true);
        nhanVien.setCheck(checker);
        nhanVien.setPHBAN(phongBan.getMAPH());

        Map.Entry<AdminData, NhanVien> dataMap = new AbstractMap.SimpleEntry<>(adminData, nhanVien);
        EntryWrapper wrapper = new EntryWrapper(dataMap);

        String json = new Gson().toJson(wrapper);

        server = new Server();
        server.postAsync("https://tester.cazo-dev.net/NT118/api/Admin/GetNhanViens", json, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(Map.Entry<String, Integer> response) {
                if (response.getValue() == 200) {
                    JsonArray jsonArray = JsonParser.parseString(response.getKey()).getAsJsonArray();
                    nhanViensInPHBAN.clear();
                    for (JsonElement element : jsonArray) {
                        // Xử lý từng phần tử JSON
                        String individualJsonString = element.toString();
                        NhanVien nhanVien1 = NhanVien.convertJsonToNhanVien(individualJsonString);

                        //Log.i("testAdmin", nhanVien1.getMANV());
                        if (nhanVien1.getMANV().equals(trph))
                            ((TextView) findViewById(R.id.txt_trph)).setText("TRPH: " + nhanVien1.getMANV() + " - " + nhanVien1.getHOTEN());
                        else nhanViensInPHBAN.add(nhanVien1);
                    }
                    nhanVienInPhongBanAdapter.notifyDataSetChanged();
                }
            }
        });

        updateNhanViensEmptyPHBAN();
    }
    private void updateNhanViensEmptyPHBAN(){
        NhanVien nhanVien = new NhanVien();
        NhanVien.Checker checker = new NhanVien.Checker();
        checker.setMANV(true);
        checker.setHOTEN(true);
        nhanVien.setCheck(checker);

        Map.Entry<AdminData, NhanVien> dataMap = new AbstractMap.SimpleEntry<>(adminData, nhanVien);
        EntryWrapper wrapper = new EntryWrapper(dataMap);

        String json = new Gson().toJson(wrapper);

        server = new Server();
        server.postAsync("https://tester.cazo-dev.net/NT118/api/Admin/GetNhanViensEmptyPHBAN", json, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(Map.Entry<String, Integer> response) {
                if (response.getValue() == 200) {
                    JsonArray jsonArray = JsonParser.parseString(response.getKey()).getAsJsonArray();
                    adapter.clearData();
                    for (JsonElement element : jsonArray) {
                        // Xử lý từng phần tử JSON
                        String individualJsonString = element.toString();
                        NhanVien nhanVien1 = NhanVien.convertJsonToNhanVien(individualJsonString);

                        //Log.i("testAdmin", nhanVien1.getMANV());
                        adapter.updateData(nhanVien1);
                    }
                }
            }
        });
    }
}