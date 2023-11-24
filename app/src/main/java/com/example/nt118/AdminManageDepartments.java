package com.example.nt118;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import com.example.nt118.Adapter.TrphArrayAdapter;
import com.example.nt118.Class.AdminData;
import com.example.nt118.Class.EntryWrapper;
import com.example.nt118.Class.NhanVien;
import com.example.nt118.Class.PhongBan;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminManageDepartments extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_departments);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String mk = intent.getStringExtra("mk");

        List<NhanVien> nhanViens = new ArrayList<>();
        List<PhongBan> phongBans = new ArrayList<>();
        nhanViens.add(new NhanVien("test","Name"));
        AutoCompleteTextView auto_complete_spinner_trph = findViewById(R.id.auto_complete_spinner_trph);
        TrphArrayAdapter adapter = new TrphArrayAdapter(this, nhanViens);
        auto_complete_spinner_trph.setAdapter(adapter);

        NhanVien nhanVien = new NhanVien();
        NhanVien.Checker checker = new NhanVien.Checker();
        checker.setMANV(true);
        checker.setHOTEN(true);
        nhanVien.setCheck(checker);
        AdminData adminData = new AdminData(id,mk);
        Map.Entry<AdminData, NhanVien> dataMap = new AbstractMap.SimpleEntry<>(adminData, nhanVien);

        EntryWrapper wrapper = new EntryWrapper(dataMap);

        Gson gson = new Gson();
        String json = gson.toJson(wrapper);

        Server server = new Server();
        server.postAsync("https://tester.cazo-dev.net/NT118/api/Admin/GetNhanViens", json, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(Map.Entry<String, Integer> response) {
                JsonArray jsonArray = JsonParser.parseString(response.getKey()).getAsJsonArray();

                for (JsonElement element : jsonArray) {
                    // Xử lý từng phần tử JSON
                    String individualJsonString = element.toString();
                    NhanVien nhanVien1 = NhanVien.convertJsonToNhanVien(individualJsonString);
                    adapter.updateData(nhanVien1);
                    //Log.i("testAdmin", nhanVien1.getMANV());
                }
            }
        });

        Map.Entry<AdminData, PhongBan> dataPhongBanEntry = new AbstractMap.SimpleEntry<>(adminData,new PhongBan());
        EntryWrapper wrapper1 = new EntryWrapper<>(dataPhongBanEntry);
        String json1 = new Gson().toJson(wrapper1);

        //Log.i("testAdmin", json1);
        server.postAsync("https://tester.cazo-dev.net/NT118/api/Admin/GetPhongBans", json1, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(Map.Entry<String, Integer> response) {
                Log.i("testAdmin", response.getKey());

                JsonArray jsonArray = JsonParser.parseString(response.getKey()).getAsJsonArray();
                for (JsonElement element : jsonArray){
                    String individualJsonString = element.toString();
                    PhongBan phongBan = PhongBan.convertJsonToPhongBan(individualJsonString);
                    phongBans.add(phongBan);
                    adapter.updateTrphData(phongBan.getTRPH());
                }
            }
        });
    }
}