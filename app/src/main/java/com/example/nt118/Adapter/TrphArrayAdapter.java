package com.example.nt118.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.nt118.Class.NhanVien;
import com.example.nt118.Class.PhongBan;
import com.example.nt118.R;

import java.util.ArrayList;
import java.util.List;

public class TrphArrayAdapter extends ArrayAdapter<NhanVien> {
    private List<NhanVien> nhanViens;
    private List<String> id_trph;
    public TrphArrayAdapter(@NonNull Context context, List<NhanVien> nhanViens) {
        super(context, 0, nhanViens);
        this.nhanViens = new ArrayList<>(nhanViens);
        this.id_trph = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_spinner_trph, parent, false
            );
        }

        ConstraintLayout constraintLayout = convertView.findViewById(R.id.constraint);
        TextView textViewName = convertView.findViewById(R.id.txt_trph);
        NhanVien item = getItem(position);

        if (position%2 != 0){
            constraintLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black1));
        } else constraintLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black));
        if (item != null) {
            String text = item.getMANV() + " - " + item.getHOTEN();
            if (item.getPHBAN() != null && !item.getPHBAN().isEmpty())
                text += " - " + item.getPHBAN();
            textViewName.setText(text);
            if (id_trph.contains(item.getMANV())){
                constraintLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue));
            }
        }

        return convertView;
    }
    public void updateData(NhanVien newNhanVien) {
        nhanViens.add(newNhanVien);
        notifyDataSetChanged();
    }
    public void clearData(){
        nhanViens.clear();
        notifyDataSetChanged();
    }
    public void updateTrphData(String trph) {
        id_trph.add(trph);
        notifyDataSetChanged();
    }
    public void clearTrphData(){
        id_trph.clear();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<NhanVien> res_suggest = new ArrayList<>();
                if (constraint == null || constraint.length() == 0){
                    res_suggest.addAll(nhanViens);
                } else {
                    String filter = constraint.toString().toLowerCase().trim();
                    for (NhanVien nhanVien : nhanViens){
                        if (nhanVien.getMANV().contains(filter)){
                            res_suggest.add(nhanVien);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = res_suggest;
                filterResults.count = res_suggest.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((List<NhanVien>)results.values);
                notifyDataSetChanged();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((NhanVien)resultValue).getMANV();
            }
        };
    }
}
