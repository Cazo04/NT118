package com.example.nt118.Adapter;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nt118.Class.PhongBan;
import com.example.nt118.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class PhongBanAdapter extends RecyclerView.Adapter<PhongBanAdapter.ViewHolder> {
    public interface OnPhongBanClickListener {
        void onPhongBanClick(int position);
        void onPhongBanLongClick(int position);
    }
    private List<PhongBan> phongBanList;
    private OnPhongBanClickListener listener;
    private Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout constraintLayout;
        private TextView textViewName;

        public ViewHolder(View view, PhongBanAdapter adapter) {
            super(view);
            constraintLayout = view.findViewById(R.id.constraint);
            textViewName = view.findViewById(R.id.txt_trph);
            view.setOnClickListener(v -> {
                if (adapter.listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        adapter.listener.onPhongBanClick(position);
                    }
                }
            });

            view.setOnLongClickListener(v -> {
                if (adapter.listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        adapter.listener.onPhongBanLongClick(position);
                        return true;
                    }
                }
                return false;
            });
        }

    }

    public PhongBanAdapter(Context context,List<PhongBan> dataSet) {
        phongBanList = dataSet;
        this.context = context;
    }
    public void setOnPhongBanClickListener(OnPhongBanClickListener listener) {
        this.listener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_spinner_trph, viewGroup, false);
        return new ViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        PhongBan phongBan = phongBanList.get(position);
        viewHolder.textViewName.setText(phongBan.getMAPH() + " - " + phongBan.getTRPH() + "\n" + new SimpleDateFormat("dd/MM/yyyy").format(phongBan.getNGNC()));
        if (position%2 != 0){
            viewHolder.constraintLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.black1));
        } else viewHolder.constraintLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
    }

    @Override
    public int getItemCount() {
        return phongBanList.size();
    }
}
