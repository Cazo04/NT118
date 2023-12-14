package com.example.nt118;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nt118.Class.NhanVien;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String manv;
    private String pass;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView avatar;
    private TextView name;
    private TextView birthDay;
    private TextView id;
    private ImageButton setting;
    private TextView gioiTinh;
    private TextView cccd;
    private TextView diaChi;
    private TextView ngayVaoLam;
    private TextView phongBan;
    private TextView sdt;
    private TextView email;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            manv = getArguments().getString(ARG_PARAM1);
            pass = getArguments().getString(ARG_PARAM2);
        }
        Server server = new Server();
        NhanVien nhanVien = new NhanVien();
        nhanVien.setMANV(manv);
        nhanVien.setMK(pass);

        String jsonString = new Gson().toJson(nhanVien);
        server.postAsync("https://s3.cazo-dev.net/NT118/api/NhanVien/GetOne", jsonString, new Server.PostResponseListener() {
            @Override
            public void onPostCompleted(Map.Entry<String, Integer> response) {
                if (response.getValue() == 200){

                    NhanVien res = NhanVien.convertJsonToNhanVien(response.getKey());
                    displayNhanVienInfo(res);
                    //Toast.makeText(getContext(), res.getHOTEN(), Toast.LENGTH_SHORT).show();
                }
                Log.e("Profile", response.getValue().toString());
            }
        });

    }
    public void displayNhanVienInfo(NhanVien nhanVien) {
        name.setText(nhanVien.getHOTEN() != null ? nhanVien.getHOTEN() : "Trống");
        birthDay.setText("Ngày sinh: " + (nhanVien.getNGSINH() != null ? formatDate(nhanVien.getNGSINH()) : "Trống"));
        id.setText("MANV: " + (nhanVien.getMANV() != null ? nhanVien.getMANV() : "Trống"));
        gioiTinh.setText("Giới tính: " +(nhanVien.getGIOITINH() != null ? nhanVien.getGIOITINH() : "Trống"));
        cccd.setText("CCCD: " + (nhanVien.getCCCD() != null ? nhanVien.getCCCD() : "Trống"));
        diaChi.setText("Địa chỉ: " + (nhanVien.getDC() != null ? nhanVien.getDC() : "Trống"));
        ngayVaoLam.setText("Ngày vào làm: " +(nhanVien.getNGVL() != null ? formatDate(nhanVien.getNGVL()) : "Trống"));
        phongBan.setText("Phòng ban: " + (nhanVien.getPHBAN() != null && !nhanVien.getPHBAN().isEmpty() ? nhanVien.getPHBAN() : "Trống"));
        sdt.setText("SĐT: " +(nhanVien.getSDT() != null && !nhanVien.getSDT().isEmpty() ? nhanVien.getSDT() : "Trống"));
        email.setText("Email: " + (nhanVien.getEMAIL() != null && !nhanVien.getEMAIL().isEmpty() ? nhanVien.getEMAIL() : "Trống"));
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        avatar = view.findViewById(R.id.avartar);
        name = view.findViewById(R.id.name);
        birthDay = view.findViewById(R.id.birth_day);
        id = view.findViewById(R.id.id);
        setting = view.findViewById(R.id.setting);
        gioiTinh = view.findViewById(R.id.gioi_tinh);
        cccd = view.findViewById(R.id.cccd);
        diaChi = view.findViewById(R.id.dia_chi);
        ngayVaoLam = view.findViewById(R.id.ngay_vao_lam);
        phongBan = view.findViewById(R.id.phong_ban);
        sdt = view.findViewById(R.id.sdt);
        email = view.findViewById(R.id.email);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Setting.class);
                getActivity().startActivity(intent);
            }
        });
    }
}