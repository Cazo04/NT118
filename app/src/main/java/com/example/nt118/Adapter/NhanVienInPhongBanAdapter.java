package com.example.nt118.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nt118.Class.NhanVien;
import com.example.nt118.Class.PhongBan;
import com.example.nt118.R;

import java.util.List;

public class NhanVienInPhongBanAdapter extends RecyclerView.Adapter<NhanVienInPhongBanAdapter.ViewHolder>{
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_spinner_trph, viewGroup, false);
        return new NhanVienInPhongBanAdapter.ViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        NhanVien nhanVien = nhanViens.get(position);
        viewHolder.textViewName.setText(nhanVien.getMANV() + " - " + nhanVien.getHOTEN());
        if (position%2 != 0){
            viewHolder.constraintLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.black1));
        } else viewHolder.constraintLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
    }

    @Override
    public int getItemCount() {
        return nhanViens.size();
    }
    private List<NhanVien> nhanViens;
    private Context context;
    public NhanVienInPhongBanAdapter(Context context, List<NhanVien> dataSet) {
        nhanViens = dataSet;
        this.context = context;
    }
    public interface OnNhanVienInPhongBanClickListener {
        void onNhanVienInPhongBanClick(int position);
        void onNhanVienInPhongBanLongClick(int position);
    }
    private NhanVienInPhongBanAdapter.OnNhanVienInPhongBanClickListener listener;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout constraintLayout;
        private TextView textViewName;

        public ViewHolder(View view, NhanVienInPhongBanAdapter adapter) {
            super(view);
            constraintLayout = view.findViewById(R.id.constraint);
            textViewName = view.findViewById(R.id.txt_trph);
            view.setOnClickListener(v -> {
                if (adapter.listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                    adapter.listener.onNhanVienInPhongBanClick(position);
                    }
                }
            });

            view.setOnLongClickListener(v -> {
                if (adapter.listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        adapter.listener.onNhanVienInPhongBanLongClick(position);
                        return true;
                    }
                }
            return false;
            });
        }
    }
    public void setOnNhanVienInPhongBanClickListener(NhanVienInPhongBanAdapter.OnNhanVienInPhongBanClickListener listener) {
        this.listener = listener;
    }
}
