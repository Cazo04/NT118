package com.example.nt118.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nt118.Class.PhongBan;
import com.example.nt118.R;

import java.util.List;

public class PhongBanAdapter extends RecyclerView.Adapter<PhongBanAdapter.ViewHolder> {

    private List<PhongBan> phongBanList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public PhongBanAdapter(List<PhongBan> dataSet) {
        phongBanList = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_spinner_trph, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        PhongBan phongBan = phongBanList.get(position);
        viewHolder.getTextView().setText(phongBan.getTRPH());
    }

    @Override
    public int getItemCount() {
        return phongBanList.size();
    }
}
