package com.example.nt118.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nt118.Class.LichLamViecData;
import com.example.nt118.LichLamViec;
import com.example.nt118.R;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private List<LichLamViecData> scheduleList;

    public ScheduleAdapter(List<LichLamViecData> scheduleList) {
        this.scheduleList = scheduleList;
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
        holder.txtTieuDe.setText(schedule.getTieuDe());
        holder.txtMoTa.setText(schedule.getMoTa());
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMalv, txtTieuDe, txtMoTa, txtNgayBatDau, txtNgayKetThuc, txtPhBan;

        public ViewHolder(View itemView) {
            super(itemView);
            txtMalv = itemView.findViewById(R.id.txt_malv);
            txtTieuDe = itemView.findViewById(R.id.txt_tieude);
            txtMoTa = itemView.findViewById(R.id.txt_mota);
            txtNgayBatDau = itemView.findViewById(R.id.txt_ngaybatdau);
            txtNgayKetThuc = itemView.findViewById(R.id.txt_ngayketthuc);
            txtPhBan = itemView.findViewById(R.id.txt_phban);
        }
    }
}
