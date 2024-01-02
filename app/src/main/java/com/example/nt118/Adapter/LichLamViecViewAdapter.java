package com.example.nt118.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nt118.Class.LichLamViecData;
import com.example.nt118.R;

import java.util.List;

public class LichLamViecViewAdapter extends RecyclerView.Adapter<LichLamViecViewAdapter.ViewHolder> {

    private List<LichLamViecData> lichLamViecDataList;
    private OnItemClickListener listener;

    public LichLamViecViewAdapter(List<LichLamViecData> lichLamViecDataList) {
        this.lichLamViecDataList = lichLamViecDataList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lichlamviecview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LichLamViecData lichLamViecData = lichLamViecDataList.get(position);
        holder.txtGio.setText(lichLamViecData.getNgayBatDau().substring(11, 16));  // Hiển thị giờ trong khoảng 11-16 ký tự
        holder.txtTieuDe.setText(lichLamViecData.getTieuDe());
        holder.txtMoTa.setText(lichLamViecData.getMoTa());
    }

    @Override
    public int getItemCount() {
        return lichLamViecDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtGio, txtTieuDe, txtMoTa;

        public ViewHolder(View itemView) {
            super(itemView);
            txtGio = itemView.findViewById(R.id.txt_gio);
            txtTieuDe = itemView.findViewById(R.id.txt_tieude);
            txtMoTa = itemView.findViewById(R.id.txt_mota);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(lichLamViecDataList.get(position));
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(LichLamViecData lichLamViecData);
    }
}