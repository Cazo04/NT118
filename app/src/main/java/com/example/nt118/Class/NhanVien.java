package com.example.nt118.Class;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NhanVien implements Serializable {
    private String MANV;
    private String MK;
    private String HOTEN;
    private String GIOITINH;
    private Date NGSINH;
    private Date NGVL;
    private String DC;
    private String SDT;
    private String EMAIL;
    private String CCCD;
    private Integer LCB;
    private String PHBAN;
    private Checker Check;

    public String getMANV() {
        return MANV;
    }

    public void setMANV(String MANV) {
        this.MANV = MANV;
    }

    public String getMK() {
        return MK;
    }

    public void setMK(String MK) {
        this.MK = MK;
    }

    public String getHOTEN() {
        return HOTEN;
    }

    public void setHOTEN(String HOTEN) {
        this.HOTEN = HOTEN;
    }

    public String getGIOITINH() {
        return GIOITINH;
    }

    public void setGIOITINH(String GIOITINH) {
        this.GIOITINH = GIOITINH;
    }

    public Date getNGSINH() {
        return NGSINH;
    }

    public void setNGSINH(Date NGSINH) {
        this.NGSINH = NGSINH;
    }

    public Date getNGVL() {
        return NGVL;
    }

    public void setNGVL(Date NGVL) {
        this.NGVL = NGVL;
    }

    public String getDC() {
        return DC;
    }

    public void setDC(String DC) {
        this.DC = DC;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public Integer getLCB() {
        return LCB;
    }

    public void setLCB(Integer LCB) {
        this.LCB = LCB;
    }

    public String getPHBAN() {
        return PHBAN;
    }

    public void setPHBAN(String PHBAN) {
        this.PHBAN = PHBAN;
    }

    public Checker getCheck() {
        return Check;
    }

    public void setCheck(Checker check) {
        Check = check;
    }

    public NhanVien(String MANV, String HOTEN) {
        this.MANV = MANV;
        this.HOTEN = HOTEN;
    }

    public NhanVien() {
    }

    public static class Checker {
        private boolean MANV;
        private boolean MK;
        private boolean HOTEN;
        private boolean GIOITINH;
        private boolean NGSINH;
        private boolean NGVL;
        private boolean DC;
        private boolean SDT;
        private boolean EMAIL;
        private boolean CCCD;
        private boolean LCB;
        private boolean PHBAN;

        public boolean isMANV() {
            return MANV;
        }

        public void setMANV(boolean MANV) {
            this.MANV = MANV;
        }

        public boolean isMK() {
            return MK;
        }

        public void setMK(boolean MK) {
            this.MK = MK;
        }

        public boolean isHOTEN() {
            return HOTEN;
        }

        public void setHOTEN(boolean HOTEN) {
            this.HOTEN = HOTEN;
        }

        public boolean isGIOITINH() {
            return GIOITINH;
        }

        public void setGIOITINH(boolean GIOITINH) {
            this.GIOITINH = GIOITINH;
        }

        public boolean isNGSINH() {
            return NGSINH;
        }

        public void setNGSINH(boolean NGSINH) {
            this.NGSINH = NGSINH;
        }

        public boolean isNGVL() {
            return NGVL;
        }

        public void setNGVL(boolean NGVL) {
            this.NGVL = NGVL;
        }

        public boolean isDC() {
            return DC;
        }

        public void setDC(boolean DC) {
            this.DC = DC;
        }

        public boolean isSDT() {
            return SDT;
        }

        public void setSDT(boolean SDT) {
            this.SDT = SDT;
        }

        public boolean isEMAIL() {
            return EMAIL;
        }

        public void setEMAIL(boolean EMAIL) {
            this.EMAIL = EMAIL;
        }

        public boolean isCCCD() {
            return CCCD;
        }

        public void setCCCD(boolean CCCD) {
            this.CCCD = CCCD;
        }

        public boolean isLCB() {
            return LCB;
        }

        public void setLCB(boolean LCB) {
            this.LCB = LCB;
        }

        public boolean isPHBAN() {
            return PHBAN;
        }

        public void setPHBAN(boolean PHBAN) {
            this.PHBAN = PHBAN;
        }

    }

    public static NhanVien convertJsonToNhanVien(String jsonStr) {
        NhanVien nhanVien = new NhanVien();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);

            nhanVien.setMANV(jsonObject.optString("manv"));
            if (!jsonObject.isNull("mk")) nhanVien.setMK(jsonObject.optString("mk"));
            if (!jsonObject.isNull("hoten")) nhanVien.setHOTEN(jsonObject.optString("hoten"));
            if (!jsonObject.isNull("gioitinh")) nhanVien.setGIOITINH(jsonObject.optString("gioitinh"));

            // Phân tích ngày sinh và ngày vào làm
            try {
                String ngSinhStr = jsonObject.optString("ngsinh");
                if (!ngSinhStr.isEmpty()) {
                    Date ngSinh = sdf.parse(ngSinhStr);
                    nhanVien.setNGSINH(ngSinh);
                }

                String ngVlStr = jsonObject.optString("ngvl");
                if (!ngVlStr.isEmpty()) {
                    Date ngVl = sdf.parse(ngVlStr);
                    nhanVien.setNGVL(ngVl);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (!jsonObject.isNull("dc")) nhanVien.setDC(jsonObject.optString("dc"));
            if (!jsonObject.isNull("sdt")) nhanVien.setSDT(jsonObject.optString("sdt"));
            if (!jsonObject.isNull("email")) nhanVien.setEMAIL(jsonObject.optString("email"));
            if (!jsonObject.isNull("cccd")) nhanVien.setCCCD(jsonObject.optString("cccd"));

            if (jsonObject.has("lcb") && !jsonObject.isNull("lcb")) {
                nhanVien.setLCB(jsonObject.getInt("lcb"));
            }

            if (!jsonObject.isNull("phban")) nhanVien.setPHBAN(jsonObject.optString("phban"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return nhanVien;
    }
}
