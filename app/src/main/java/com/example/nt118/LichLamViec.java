package com.example.nt118;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nt118.Adapter.ScheduleAdapter;
import com.example.nt118.Class.LichLamViecData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LichLamViec extends AppCompatActivity {
    private EditText editTextTieuDe, editTextMoTa, editTextPhBan;
    private Button buttonAdd, buttonUpdate, buttonDelete;
    private TextView textViewNgayBatDau, textViewNgayKetThuc;
    private RecyclerView recyclerView;
    private Calendar calendar;
    private int year, month, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_lam_viec);
        // Khai báo EditTexts
        editTextTieuDe = findViewById(R.id.editTextTieuDe);
        editTextMoTa = findViewById(R.id.editTextMoTa);
        editTextPhBan = findViewById(R.id.editTextPhBan);

        // Khai báo TextViews cho DatePicker
        textViewNgayBatDau = findViewById(R.id.textViewNgayBatDau);
        textViewNgayKetThuc = findViewById(R.id.textViewNgayKetThuc);
        calendar = Calendar.getInstance();

        textViewNgayBatDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(textViewNgayBatDau);
            }
        });

        textViewNgayKetThuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(textViewNgayKetThuc);
            }
        });

        // Khai báo Buttons
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);

        // Xử lý sự kiện nhấn nút
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thêm mã xử lý cho nút Thêm ở đây
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thêm mã xử lý cho nút Cập nhật ở đây
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thêm mã xử lý cho nút Xóa ở đây
            }
        });

        recyclerView = findViewById(R.id.recyclerViewSchedule);
        List<LichLamViecData> lichLamViecData = new ArrayList<>();
        ScheduleAdapter adapter = new ScheduleAdapter(lichLamViecData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                if (swipeDir == ItemTouchHelper.LEFT) {
                    // Xử lý khi kéo sang trái: Xóa mục
                    //scheduleList.remove(position);
                    //adapter.notifyItemRemoved(position);
                } else if (swipeDir == ItemTouchHelper.RIGHT) {
                    // Xử lý khi kéo sang phải: Mở Activity khác
                    //openAnotherActivity();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

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
}