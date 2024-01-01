package com.example.nt118;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nt118.Class.EntryWrapper;
import com.example.nt118.Class.NhanVien;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EmployeeManager extends AppCompatActivity {
    private NhanVien root;
    private List<NhanVien> nhanViensInPHBan;
    private ArrayAdapter<String> adapter;
    private List<String> datas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_manager);
        Intent intent = getIntent();
        root = (NhanVien) intent.getSerializableExtra("nhanVien");

        nhanViensInPHBan = new ArrayList<>();
        datas = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.item_list, datas);
        ListView list_nhan_vien = findViewById(R.id.list_nhanvien);
        list_nhan_vien.setAdapter(adapter);

        list_nhan_vien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getNhanVien(nhanViensInPHBan.get(position).getMANV());
            }
        });

        getNhanViensInPHban();
    }
    private void getNhanVien(String manv){
        NhanVien nhanVien = new NhanVien();
        nhanVien.setMANV(manv);

        String json = new Gson().toJson(new EntryWrapper(new AbstractMap.SimpleEntry(root, nhanVien)));
        Server server = new Server();
        server.postAsync("https://s3.cazo-dev.net/NT118/api/Trph/GetNhanVien", json, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(Map.Entry<String, Integer> response) {
                if (response.getValue() == 200) {
                    NhanVien res = NhanVien.convertJsonToNhanVien(response.getKey());

                    AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeManager.this);

                    builder.setTitle("Thông tin nhân viên");
                    String mess = "Mã nhân viên: " + res.getMANV() + "\n"
                            + "Họ tên: " + res.getHOTEN() + "\n"
                            + "Giới tính: " + res.getGIOITINH() + "\n"
                            + "Ngày sinh: " + (res.getNGSINH() != null ? formatDate(res.getNGSINH()) : "Trống") + "\n"
                            + "SĐT: " + (res.getSDT() != null && !res.getSDT().isEmpty() ? res.getSDT() : "Trống") + "\n"
                            + "Email: " + (res.getEMAIL() != null && !res.getEMAIL().isEmpty() ? res.getEMAIL() : "Trống");
                    builder.setMessage(mess);

                    if (res.getEMAIL() != null && !res.getEMAIL().isEmpty())
                        builder.setNeutralButton("Sao chép email", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("label", res.getEMAIL());
                                clipboard.setPrimaryClip(clip);

                                Toast.makeText(EmployeeManager.this, "Đã sao chép vào bộ nhớ tạm", Toast.LENGTH_SHORT).show();
                            }
                        });
                    if (res.getSDT() != null && !res.getSDT().isEmpty())
                        builder.setNegativeButton("Gọi", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + res.getSDT()));
                                startActivity(intent);
                            }
                        });
                    builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(false);

                    dialog.show();
                } else
                    showErrorRequest(response.getValue().toString() + " " + response.getKey());
            }
        });
    }
    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }
    private void getNhanViensInPHban(){
        nhanViensInPHBan.clear();
        datas.clear();

        NhanVien nhanVien = new NhanVien();
        NhanVien.Checker checker = new NhanVien.Checker();
        checker.setMANV(true);
        checker.setHOTEN(true);
        checker.setPHBAN(true);
        nhanVien.setCheck(checker);
        nhanVien.setPHBAN(root.getPHBAN());

        String json = new Gson().toJson(new EntryWrapper(new AbstractMap.SimpleEntry(root, nhanVien)));
        Server server = new Server();
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
                        if (!nhanVien1.getMANV().equals(root.getMANV())) {
                            nhanViensInPHBan.add(nhanVien1);
                            datas.add(nhanVien1.getMANV() + " - " + nhanVien1.getHOTEN());
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else
                    showErrorRequest(response.getValue().toString() + " " + response.getKey());
            }
        });
    }
    private void showErrorRequest(String mess){
        Toast.makeText(this, "Response code: " + mess, Toast.LENGTH_SHORT).show();
    }
}