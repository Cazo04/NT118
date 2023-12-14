package com.example.nt118.Class;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LichLamViecData {
    private Integer MaLV;
    private String TieuDe;
    private String MoTa;
    private String NgayBatDau;
    private String NgayKetThuc;
    private Integer SoLuongNhanVien;

    public Integer getSoLuongNhanVien() {
        return SoLuongNhanVien;
    }

    public void setSoLuongNhanVien(Integer soLuongNhanVien) {
        SoLuongNhanVien = soLuongNhanVien;
    }

    public String getNgayBatDau() {
        return NgayBatDau;
    }

    public void setNgayBatDau(String ngayBatDau) {
        NgayBatDau = ngayBatDau;
    }

    public String getNgayKetThuc() {
        return NgayKetThuc;
    }

    public void setNgayKetThuc(String ngayKetThuc) {
        NgayKetThuc = ngayKetThuc;
    }

    private String PhBan;

    public Integer getMaLV() {
        return MaLV;
    }

    public void setMaLV(Integer maLV) {
        MaLV = maLV;
    }

    public String getTieuDe() {
        return TieuDe;
    }

    public void setTieuDe(String tieuDe) {
        TieuDe = tieuDe;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }

    public String getPhBan() {
        return PhBan;
    }

    public void setPhBan(String phBan) {
        PhBan = phBan;
    }

    public static LichLamViecData convertJsonToLichLamViecData(String jsonStr) {
        LichLamViecData lichLamViecData = new LichLamViecData();

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.has("maLV") && !jsonObject.isNull("maLV")) {
                lichLamViecData.setMaLV(jsonObject.getInt("maLV"));
            }

            lichLamViecData.setTieuDe(jsonObject.optString("tieuDe"));
            lichLamViecData.setMoTa(jsonObject.optString("moTa"));
            lichLamViecData.setNgayBatDau(jsonObject.optString("ngayBatDau"));
            lichLamViecData.setNgayKetThuc(jsonObject.optString("ngayKetThuc"));

            if (jsonObject.has("soLuongNhanVien") && !jsonObject.isNull("soLuongNhanVien")) {
                lichLamViecData.setSoLuongNhanVien(jsonObject.getInt("soLuongNhanVien"));
            }

            lichLamViecData.setPhBan(jsonObject.optString("phBan"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return lichLamViecData;
    }
    public static List<LichLamViecData> convertJsonToList(String jsonStr){
        JsonArray jsonArray = JsonParser.parseString(jsonStr).getAsJsonArray();
        List<LichLamViecData> res = new ArrayList<>();

        for (JsonElement element : jsonArray) {
            String individualJsonString = element.toString();
            LichLamViecData data = convertJsonToLichLamViecData(individualJsonString);
            res.add(data);
        }
        return res;
    }
}
