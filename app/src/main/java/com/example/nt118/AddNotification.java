package com.example.nt118;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nt118.Class.EntryWrapper;
import com.example.nt118.Class.NhanVien;
import com.example.nt118.Class.NotificationManagerData;
import com.google.gson.Gson;

import java.util.AbstractMap;
import java.util.Map;

public class AddNotification extends AppCompatActivity {
    private TextView tvPhongBan;
    private EditText etNoiDung;
    private Button btnGui;
    private NhanVien root;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);
        Intent intent = getIntent();
        root = (NhanVien) intent.getSerializableExtra("nhanVien");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang thực hiện POST...");
        progressDialog.setCancelable(false);

        tvPhongBan = findViewById(R.id.tvPhongBan);
        etNoiDung = findViewById(R.id.etNoiDung);
        btnGui = findViewById(R.id.btnGui);

        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
    }
    private void sendNotification() {
        String noiDung = etNoiDung.getText().toString();
        Server server = new Server();
        NotificationManagerData data = new NotificationManagerData();
        data.setContent(noiDung);
        data.setPhban(root.getPHBAN());

        String json = new Gson().toJson(new EntryWrapper(new AbstractMap.SimpleEntry(root, data)));

        progressDialog.show();
        server.postAsync("https://s3.cazo-dev.net/NT118/api/Trph/InsertNotification", json, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(Map.Entry<String, Integer> response) {
                if (response.getValue() == 200){
                    Toast.makeText(AddNotification.this, "Thêm thông báo thành công!", Toast.LENGTH_SHORT).show();
                    AddNotification.this.finish();
                } else showErrorRequest(response.getValue().toString() + " " + response.getKey());
                progressDialog.dismiss();
            }
        });
    }
    private void showErrorRequest(String mess){
        Toast.makeText(this, "Response code: " + mess, Toast.LENGTH_SHORT).show();
    }
}