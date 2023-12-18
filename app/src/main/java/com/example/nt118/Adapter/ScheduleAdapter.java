package com.example.nt118.Adapter;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nt118.Class.LichLamViecData;
import com.example.nt118.LichLamViec;
import com.example.nt118.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    public interface OnItemDoubleClickListener {
        void onItemDoubleClick(int position);
    }
    private OnItemDoubleClickListener doubleClickListener;
    private List<LichLamViecData> scheduleList;

    public ScheduleAdapter(List<LichLamViecData> scheduleList) {
        this.scheduleList = scheduleList;
    }
    public void setOnItemDoubleClickListener(OnItemDoubleClickListener listener) {
        this.doubleClickListener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lichlamviec, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LichLamViecData schedule = scheduleList.get(position);
        holder.txtMalv.setText(String.valueOf(schedule.getMaLV()));
        holder.txtTieuDe.setText("Tiêu đề: " + schedule.getTieuDe());
        holder.txtMoTa.setText("Mô tả: " + schedule.getMoTa());
        holder.txtNgayBatDau.setText("Bắt đầu: " + schedule.getNgayBatDau());
        holder.txtNgayKetThuc.setText("Kết thúc: " + schedule.getNgayKetThuc());
        holder.txt_soluongnhanvien.setText("Số lượng nhân viên: " + schedule.getSoLuongNhanVien().toString());
        GestureDetector gestureDetector = new GestureDetector(holder.itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (doubleClickListener != null) {
                    doubleClickListener.onItemDoubleClick(holder.getAdapterPosition());
                }
                return true;
            }
        });

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMalv, txtTieuDe, txtMoTa, txtNgayBatDau, txtNgayKetThuc, txt_soluongnhanvien;

        public ViewHolder(View itemView) {
            super(itemView);
            txtMalv = itemView.findViewById(R.id.txt_malv);
            txtTieuDe = itemView.findViewById(R.id.txt_tieude);
            txtMoTa = itemView.findViewById(R.id.txt_mota);
            txtNgayBatDau = itemView.findViewById(R.id.txt_ngaybatdau);
            txtNgayKetThuc = itemView.findViewById(R.id.txt_ngayketthuc);
            txt_soluongnhanvien = itemView.findViewById(R.id.txt_soluongnhanvien);
        }
    }
}
