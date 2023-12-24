package com.example.nt118;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nt118.Adapter.NhanVienInPhongBanAdapter;
import com.example.nt118.Adapter.TrphArrayAdapter;
import com.example.nt118.Class.AdminData;
import com.example.nt118.Class.EntryWrapper;
import com.example.nt118.Class.LichLamViecData;
import com.example.nt118.Class.NhanVien;
import com.example.nt118.Class.ThamGiaLamViecData;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LichLamViecNhanVien extends AppCompatActivity {

    private NhanVien root;
    private List<NhanVien> nhanViensInPHBan;
    private int malv;
    private Server server;
    private RecyclerView recyclerView;
    private AutoCompleteTextView auto_complete_spinner_manv;
    private TrphArrayAdapter adapter;
    private TextView txt_malv;
    private List<NhanVien> nhanViensInLV;
    private NhanVienInPhongBanAdapter nhanVienInPhongBanAdapter;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_lam_viec_nhan_vien);
        Intent intent = getIntent();
        root = (NhanVien) intent.getSerializableExtra("nhanVien");
        malv = intent.getIntExtra("malv",0);
        txt_malv = findViewById(R.id.txt_malv);
        txt_malv.setText("MALV: " + String.valueOf(malv));
        nhanViensInPHBan = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang thực hiện POST...");
        progressDialog.setCancelable(false);

        server = new Server();

        auto_complete_spinner_manv = findViewById(R.id.auto_complete_spinner_manv);
        adapter = new TrphArrayAdapter(this, nhanViensInPHBan);
        auto_complete_spinner_manv.setAdapter(adapter);

        getNhanViensInPHban();

        nhanViensInLV = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        nhanVienInPhongBanAdapter = new NhanVienInPhongBanAdapter(this, nhanViensInLV);
        recyclerView.setAdapter(nhanVienInPhongBanAdapter);
        getNhanViensInLV();
        ((Button)findViewById(R.id.btn_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String manv = auto_complete_spinner_manv.getText().toString().trim();
                ThamGiaLamViecData data = new ThamGiaLamViecData();
                data.setMALV(malv);
                data.setMANV(manv);

                String json = new Gson().toJson(new EntryWrapper(new AbstractMap.SimpleEntry(root, data)));

                server.postAsync("https://s3.cazo-dev.net/NT118/api/Trph/ThemNhanVienVaoCongViec", json, new Server.PostResponseListener() {
                    @Override
                    public void onPostCompleted(Map.Entry<String, Integer> response) {
                        if (response.getValue() == 200) {
                            Toast.makeText(LichLamViecNhanVien.this, "Thêm nhân viên vào công việc thành công", Toast.LENGTH_SHORT).show();
                            getNhanViensInLV();
                        } else
                            errorResponse(response.getValue().toString() + " " + response.getKey());
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
                String manv = nhanViensInLV.get(position).getMANV();
                ThamGiaLamViecData data = new ThamGiaLamViecData();
                data.setMALV(malv);
                data.setMANV(manv);

                String json = new Gson().toJson(new EntryWrapper(new AbstractMap.SimpleEntry(root, data)));
                progressDialog.show();
                server.postAsync("https://s3.cazo-dev.net/NT118/api/Trph/XoaNhanVienKhoiCongViec", json, new Server.PostResponseListener() {
                    @Override
                    public void onPostCompleted(Map.Entry<String, Integer> response) {
                        if (response.getValue() == 200) {
                            Toast.makeText(LichLamViecNhanVien.this, "Xóa nhân viên khỏi công việc thành công", Toast.LENGTH_SHORT).show();
                            getNhanViensInLV();
                        } else
                            errorResponse(response.getValue().toString() + " " + response.getKey());
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }
    private void loadData(){

    }
    private void errorResponse(String mess){
        Toast.makeText(LichLamViecNhanVien.this, "Response code: " + mess, Toast.LENGTH_SHORT).show();
    }
    private void getNhanViensInPHban(){
        nhanViensInPHBan.clear();
        adapter.clearData();

        NhanVien nhanVien = new NhanVien();
        NhanVien.Checker checker = new NhanVien.Checker();
        checker.setMANV(true);
        checker.setHOTEN(true);
        checker.setPHBAN(true);
        nhanVien.setCheck(checker);
        nhanVien.setPHBAN(root.getPHBAN());

        String json = new Gson().toJson(new EntryWrapper(new AbstractMap.SimpleEntry(root, nhanVien)));

        server.postAsync("https://s3.cazo-dev.net/NT118/api/Trph/GetNhanViens", json, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(Map.Entry<String, Integer> response) {
                if (response.getValue() == 200) {
                    JsonArray jsonArray = JsonParser.parseString(response.getKey()).getAsJsonArray();
                    for (JsonElement element : jsonArray) {
                        // Xử lý từng phần tử JSON
                        String individualJsonString = element.toString();
                        NhanVien nhanVien1 = NhanVien.convertJsonToNhanVien(individualJsonString);

                        //Log.i("testAdmin", nhanVien1.getMANV());
                        if (!nhanVien1.getMANV().equals(root.getMANV()) && !nhanViensInLV.stream().anyMatch(nv -> nv.getMANV().equals(nhanVien1.getMANV())))
                            adapter.updateData(nhanVien1);
                    }
                } else
                    errorResponse(response.getValue().toString() + " " + response.getKey());
            }
        });
    }
    private void getNhanViensInLV(){
        LichLamViecData data = new LichLamViecData();
        data.setMaLV(malv);
        data.setPhBan(root.getPHBAN());

        String json = new Gson().toJson(new EntryWrapper(new AbstractMap.SimpleEntry(root, data)));

        server.postAsync("https://s3.cazo-dev.net/NT118/api/Trph/LayDanhSachNhanVienTrongCongViecCuaPhongBan", json, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(Map.Entry<String, Integer> response) {
                if (response.getValue() == 200) {
                    nhanViensInLV.clear();
                    JsonArray jsonArray = JsonParser.parseString(response.getKey()).getAsJsonArray();
                    for (JsonElement element : jsonArray) {
                        // Xử lý từng phần tử JSON
                        String individualJsonString = element.toString();
                        NhanVien nhanVien1 = NhanVien.convertJsonToNhanVien(individualJsonString);

                        //Log.i("testAdmin", nhanVien1.getMANV());
                        if (!nhanVien1.getMANV().equals(root.getMANV()))
                            nhanViensInLV.add(nhanVien1);
                    }
                    nhanVienInPhongBanAdapter.notifyDataSetChanged();
                    getNhanViensInPHban();
                } else
                    errorResponse(response.getValue().toString() + " " + response.getKey());
            }
        });
    }
}