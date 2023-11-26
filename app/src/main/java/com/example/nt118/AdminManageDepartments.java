package com.example.nt118;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nt118.Adapter.PhongBanAdapter;
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
    private List<PhongBan> phongBans;
    private ProgressDialog progressDialog;
    private PhongBanAdapter phongBanAdapter;
    private TrphArrayAdapter adapter;
    private AdminData adminData;
    private Server server;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_departments);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String mk = intent.getStringExtra("mk");
        adminData = new AdminData(id,mk);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang thực hiện POST...");
        progressDialog.setCancelable(false);

        phongBans = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        phongBanAdapter = new PhongBanAdapter(this, phongBans);
        recyclerView.setAdapter(phongBanAdapter);

        List<NhanVien> nhanViens = new ArrayList<>();
        nhanViens.add(new NhanVien("test","Name"));
        AutoCompleteTextView auto_complete_spinner_trph = findViewById(R.id.auto_complete_spinner_trph);
        adapter = new TrphArrayAdapter(this, nhanViens);
        auto_complete_spinner_trph.setAdapter(adapter);



        updateView();
        //Log.i("testAdmin", json1);


        EditText edit_maph = findViewById(R.id.edit_maph);
        Button btn_cancel = findViewById(R.id.btn_cancel);
        Button btn_add = findViewById(R.id.btn_add);
        Button btn_apply = findViewById(R.id.btn_apply);
        Button btn_more = findViewById(R.id.btn_more);
        btn_apply.setEnabled(false);
        btn_more.setEnabled(false);
        phongBanAdapter.setOnPhongBanClickListener(new PhongBanAdapter.OnPhongBanClickListener() {
            @Override
            public void onPhongBanClick(int position) {
                edit_maph.setText(phongBans.get(position).getMAPH());
                auto_complete_spinner_trph.setText(phongBans.get(position).getTRPH());
                btn_apply.setEnabled(true);
                btn_more.setEnabled(true);
                btn_add.setEnabled(false);
            }

            @Override
            public void onPhongBanLongClick(int position) {
                progressDialog.show();
                String maph = phongBans.get(position).getMAPH();
                PhongBan phongBan = new PhongBan();
                phongBan.setMAPH(maph);
                EntryWrapper entryWrapper = new EntryWrapper(new AbstractMap.SimpleEntry<>(adminData,phongBan));
                String postData = new Gson().toJson(entryWrapper);
                server.postAsync("https://tester.cazo-dev.net/NT118/api/Admin/DeletePhongBan", postData, new Server.PostResponseListener() {
                    @Override
                    public void onPostCompleted(Map.Entry<String, Integer> response) {
                        if (response.getValue() == 200){
                            updateView();
                        } else
                            Toast.makeText(AdminManageDepartments.this, "DeletePhongBan Error: " + response.getKey().toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_maph.setText("");
                auto_complete_spinner_trph.setText("");
                btn_apply.setEnabled(false);
                btn_more.setEnabled(false);
                btn_add.setEnabled(true);

            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String trph = auto_complete_spinner_trph.getText().toString().trim();
                PhongBan phongBan = new PhongBan();
                phongBan.setTRPH(trph);
                EntryWrapper entryWrapper = new EntryWrapper(new AbstractMap.SimpleEntry<>(adminData,phongBan));
                String postData = new Gson().toJson(entryWrapper);
                server.postAsync("https://tester.cazo-dev.net/NT118/api/Admin/InsertPhongBan", postData, new Server.PostResponseListener() {
                    @Override
                    public void onPostCompleted(Map.Entry<String, Integer> response) {
                        if (response.getValue() == 200){
                            updateView();
                        } else
                            Toast.makeText(AdminManageDepartments.this, "InsertPhongBan Error: " + response.getKey().toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String trph = auto_complete_spinner_trph.getText().toString().trim();
                String maph = edit_maph.getText().toString().trim();
                PhongBan phongBan = new PhongBan();
                phongBan.setTRPH(trph);
                phongBan.setMAPH(maph);
                EntryWrapper entryWrapper = new EntryWrapper(new AbstractMap.SimpleEntry<>(adminData,phongBan));
                String postData = new Gson().toJson(entryWrapper);
                server.postAsync("https://tester.cazo-dev.net/NT118/api/Admin/UpdatePhongBan", postData, new Server.PostResponseListener() {
                    @Override
                    public void onPostCompleted(Map.Entry<String, Integer> response) {
                        if (response.getValue() == 200){
                            updateView();
                        } else
                            Toast.makeText(AdminManageDepartments.this, "UpdatePhongBan Error: " + response.getKey().toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });
        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminManageDepartments.this, AdminManageDepartmentsMoreDetails.class);
                intent.putExtra("id", id);
                intent.putExtra("mk", mk);
                intent.putExtra("maph", edit_maph.getText().toString().trim());
                for (PhongBan phongBan : phongBans){
                    if (phongBan.getMAPH().equals(edit_maph.getText().toString().trim())){
                        intent.putExtra("trph", phongBan.getTRPH());
                        break;
                    }
                }
                startActivityForResult(intent, 100);
            }
        });
    }
    private void updateView(){
        NhanVien nhanVien = new NhanVien();
        NhanVien.Checker checker = new NhanVien.Checker();
        checker.setMANV(true);
        checker.setHOTEN(true);
        checker.setPHBAN(true);
        nhanVien.setCheck(checker);

        Map.Entry<AdminData, NhanVien> dataMap = new AbstractMap.SimpleEntry<>(adminData, nhanVien);

        EntryWrapper wrapper = new EntryWrapper(dataMap);

        Gson gson = new Gson();
        String json = gson.toJson(wrapper);

        server = new Server();
        server.postAsync("https://tester.cazo-dev.net/NT118/api/Admin/GetNhanViens", json, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(Map.Entry<String, Integer> response) {
                JsonArray jsonArray = JsonParser.parseString(response.getKey()).getAsJsonArray();
                adapter.clearData();
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
        server.postAsync("https://tester.cazo-dev.net/NT118/api/Admin/GetPhongBans", json1, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(Map.Entry<String, Integer> response) {
                if (response.getValue() == 200){
                    phongBans.clear();
                    adapter.clearTrphData();

                    JsonArray jsonArray = JsonParser.parseString(response.getKey()).getAsJsonArray();
                    for (JsonElement element : jsonArray){
                        String individualJsonString = element.toString();
                        PhongBan phongBan = PhongBan.convertJsonToPhongBan(individualJsonString);
                        phongBans.add(phongBan);
                        adapter.updateTrphData(phongBan.getTRPH());
                    }
                    phongBanAdapter.notifyDataSetChanged();
                } else
                    Toast.makeText(AdminManageDepartments.this, "GetPhongBans Error: " + response.getKey().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {

        }
    }
}