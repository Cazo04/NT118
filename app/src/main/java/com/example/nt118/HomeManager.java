package com.example.nt118;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nt118.Class.NhanVien;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeManager#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeManager extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String manv;
    private String mk;
    private String phban;

    public HomeManager() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeManager.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeManager newInstance(String param1, String param2, String param3) {
        HomeManager fragment = new HomeManager();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }
    private Button btn_lich_nhan_vien;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            manv = getArguments().getString(ARG_PARAM1);
            mk = getArguments().getString(ARG_PARAM2);
            phban = getArguments().getString(ARG_PARAM3);
            nhanVien = new NhanVien();
            nhanVien.setMANV(manv);
            nhanVien.setMK(mk);
            nhanVien.setPHBAN(phban);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_manager, container, false);
        return view;
    }
    private NhanVien nhanVien;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnLichNhanVien = view.findViewById(R.id.btn_lich_nhan_vien);
        Button btnQuanLyNhanVien = view.findViewById(R.id.btn_quan_ly_nhan_vien);
        Button btnDuyetDon = view.findViewById(R.id.btn_duyet_don);
        Button btnXuLyViPham = view.findViewById(R.id.btn_xu_ly_vi_pham);
        Button btnPhanHoiYKien = view.findViewById(R.id.btn_phan_hoi_y_kien);
        Button btnLuongNhanvien = view.findViewById(R.id.btn_luong_nhanvien);

        btnLichNhanVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LichLamViec.class);
                intent.putExtra("nhanVien", nhanVien);
                startActivity(intent);
            }
        });

        btnQuanLyNhanVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EmployeeManager.class);
                intent.putExtra("nhanVien", nhanVien);
                startActivity(intent);
            }
        });

        btnDuyetDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi btnDuyetDon được nhấn
            }
        });

        btnXuLyViPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi btnXuLyViPham được nhấn
            }
        });

        btnPhanHoiYKien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi btnPhanHoiYKien được nhấn
            }
        });
        btnLuongNhanvien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}