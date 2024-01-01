package com.example.nt118.Adapter;

import static com.example.nt118.R.color.green;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nt118.Class.Luong;
import com.example.nt118.R;

import java.util.List;

public class LuongAdapter extends RecyclerView.Adapter<LuongAdapter.LuongViewHolder> {
    private List<Luong> luongList;

    public LuongAdapter(List<Luong> luongList) {
        this.luongList = luongList;
    }

    @NonNull
    @Override
    public LuongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_luong, parent, false);
        return new LuongViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull LuongViewHolder holder, int position) {
        Luong luong = luongList.get(position);
        if (luong == null) {
            return;
        }
        holder.tv_tong_luong.setText(luong.getTong_thu_nhap());
        holder.tv_ky_thanh_toan.setText(luong.getKy_thanh_toan());
        if(luong.isDa_thanh_toan() == true){
            holder.tv_ngay_thanh_toan.setText(luong.getNgay_thanh_toan());
            holder.tv_ngay_thanh_toan.setTextColor(R.color.green);
            holder.tv_trang_thai_thanh_toan.setText("Đã thanh toán");
            holder.tv_trang_thai_thanh_toan.setTextColor(R.color.green);
        } else if (luong.isDa_thanh_toan() == false) {
            holder.tv_trang_thai_thanh_toan.setText("Chưa thanh toán");
            holder.tv_trang_thai_thanh_toan.setTextColor(R.color.red);
        }
    }

    @Override
    public int getItemCount() {
        if (luongList != null) {
            return luongList.size();
        }
        return 0;
    }

    class LuongViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_ky_thanh_toan, tv_tong_luong, tv_ngay_thanh_toan, tv_trang_thai_thanh_toan;
        public LuongViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tong_luong = itemView.findViewById(R.id.tv_tong_luong);
            tv_ky_thanh_toan = itemView.findViewById(R.id.tv_ky_thanh_toan);
            tv_ngay_thanh_toan = itemView.findViewById(R.id.tv_ngay_thanh_toan);
            tv_trang_thai_thanh_toan = itemView.findViewById(R.id.tv_trang_thai_thanh_toan);
        }
    }
}
