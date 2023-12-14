package com.example.nt118;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nt118.Adapter.ScheduleAdapter;
import com.example.nt118.Class.EntryWrapper;
import com.example.nt118.Class.LichLamViecData;
import com.example.nt118.Class.NhanVien;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LichLamViec extends AppCompatActivity {
    private static final String DEFAULT_TIEUDE = "Công việc bình thường";
    private static final String DEFAULT_MOTA = "Như thường lệ";
    private EditText editTextTieuDe, editTextMoTa, editTextPhBan;
    private Button buttonAdd, buttonUpdate, buttonCancel;
    private TextView textViewNgayBatDau, textViewNgayKetThuc;
    private TextView textViewThoiGianBD, textViewThoiGianKT;
    private Calendar calendar;
    private int year, month, day;
    private NhanVien nhanVien;
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private List<LichLamViecData> lichLamViecDataList;
    private Server server;
    private String ngayBatDau, ngayKetThuc;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_lam_viec);
        server = new Server();
        progressDialog = new ProgressDialog(LichLamViec.this);
        progressDialog.setMessage("Đang thực hiện POST...");
        progressDialog.setCancelable(false);

        Intent intent = getIntent();
        nhanVien = (NhanVien) intent.getSerializableExtra("nhanVien");

        lichLamViecDataList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewSchedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScheduleAdapter(lichLamViecDataList);
        recyclerView.setAdapter(adapter);

        loadData();

        editTextTieuDe = findViewById(R.id.editTextTieuDe);
        editTextMoTa = findViewById(R.id.editTextMoTa);
        editTextPhBan = findViewById(R.id.editTextPhBan);
        editTextPhBan.setText("Phòng ban: " + nhanVien.getPHBAN());
        editTextPhBan.setEnabled(false);

        textViewNgayBatDau = findViewById(R.id.textViewNgayBatDau);
        textViewNgayKetThuc = findViewById(R.id.textViewNgayKetThuc);
        calendar = Calendar.getInstance();

        textViewNgayBatDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(textViewNgayBatDau);
                if (textViewThoiGianBD.getText().toString().isEmpty())
                    textViewThoiGianBD.setText("07:30");
            }
        });

        textViewNgayKetThuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(textViewNgayKetThuc);
                if (textViewThoiGianKT.getText().toString().isEmpty())
                    textViewThoiGianKT.setText("19:00");
            }
        });

        textViewThoiGianBD = findViewById(R.id.tvTimePickerDB);
        textViewThoiGianKT = findViewById(R.id.tvTimePickerKT);
        textViewThoiGianBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(textViewThoiGianBD);
                if (textViewNgayKetThuc.getText().toString().isEmpty()){
                    textViewNgayKetThuc.setText(textViewNgayBatDau.getText());
                    if (textViewThoiGianKT.getText().toString().isEmpty())
                        textViewThoiGianKT.setText("19:00");
                }
            }
        });
        textViewThoiGianKT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(textViewThoiGianKT);
            }
        });

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonCancel = findViewById(R.id.buttonCancel);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!convertTextToDate()) return;
                LichLamViecData data = new LichLamViecData();
                data.setNgayBatDau(ngayBatDau);
                data.setNgayKetThuc(ngayKetThuc);
                if (editTextTieuDe.getText().toString().trim().isEmpty()){
                    editTextTieuDe.setText(DEFAULT_TIEUDE);
                }
                if (editTextMoTa.getText().toString().trim().isEmpty()){
                    editTextMoTa.setText(DEFAULT_MOTA);
                }
                data.setTieuDe(editTextTieuDe.getText().toString().trim());
                data.setMoTa(editTextMoTa.getText().toString().trim());
                data.setPhBan(nhanVien.getPHBAN());

                //
                String json = new Gson().toJson(new EntryWrapper(new AbstractMap.SimpleEntry(nhanVien, data)));

                progressDialog.show();
                server.postAsync("https://s3.cazo-dev.net/NT118/api/Trph/ThemCongViec", json, new Server.PostResponseListener() {
                    @Override
                    public void onPostCompleted(Map.Entry<String, Integer> response) {
                        if (response.getValue() == 200){
                            Toast.makeText(LichLamViec.this, "Thêm công việc thành công!", Toast.LENGTH_SHORT).show();
                        } else showErrorRequest(response.getValue().toString() + " " + response.getKey());
                        progressDialog.dismiss();
                    }
                });
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thêm mã xử lý cho nút Cập nhật ở đây
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thêm mã xử lý cho nút Xóa ở đây
            }
        });
    }
    private void showErrorRequest(String mess){
        Toast.makeText(LichLamViec.this, "Response code: " + mess, Toast.LENGTH_SHORT).show();
    }
    private void showDatePickerDialog(final TextView textView) {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Cập nhật TextView với ngày được chọn
                        textView.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }
    private void showTimePickerDialog(TextView tvTimePicker) {
        // Lấy thời gian hiện tại
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Tạo mới TimePickerDialog và hiển thị
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                tvTimePicker.setText(selectedTime);
            }
        }, hour, minute, true); // 'true' cho định dạng 24 giờ

        timePickerDialog.show();
    }
    private boolean convertTextToDate(){
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dateTimeFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        String strNgayBatDau = textViewNgayBatDau.getText().toString();
        String strNgayKetThuc = textViewNgayKetThuc.getText().toString();

        String strThoiGianBD = textViewThoiGianBD.getText().toString();
        String strThoiGianKT = textViewThoiGianKT.getText().toString();

        try {
            String strDateTimeBD = strNgayBatDau + " " + strThoiGianBD;
            String strDateTimeKT = strNgayKetThuc + " " + strThoiGianKT;

            ngayBatDau = dateTimeFormat.format(dateTimeFormat2.parse(strDateTimeBD));
            ngayKetThuc = dateTimeFormat.format(dateTimeFormat2.parse(strDateTimeKT));
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    private void loadData(){
        LayDanhSachCongViecCuaPhongBan_Limit10(0);
    }
    private void LayDanhSachCongViecCuaPhongBan_Limit10(int offset){
        LichLamViecData data = new LichLamViecData();
        data.setPhBan(nhanVien.getPHBAN());
        data.setMaLV(offset);

        String json = new Gson().toJson(new EntryWrapper(new AbstractMap.SimpleEntry(nhanVien, data)));
        server.postAsync("https://s3.cazo-dev.net/NT118/api/Trph/LayDanhSachCongViecCuaPhongBan_Limit10", json, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(Map.Entry<String, Integer> response) {
                if (response.getValue() == 200){

                    //Toast.makeText(LichLamViec.this, "", Toast.LENGTH_SHORT).show();
                    //Log.i("GetJob", response.getKey());
                    lichLamViecDataList.clear();
                    for (LichLamViecData data1 : LichLamViecData.convertJsonToList(response.getKey())){
                        lichLamViecDataList.add(data1);
                    }
                    adapter.notifyDataSetChanged();

                } else showErrorRequest(response.getValue().toString() + " " + response.getKey());
            }
        });
    }
}