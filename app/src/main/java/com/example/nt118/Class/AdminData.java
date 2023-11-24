package com.example.nt118.Class;

public class AdminData {
    private int Id;
    private String Ten;
    private String MK;

    public AdminData() {
    }

    public AdminData(int id, String MK) {
        Id = id;
        this.MK = MK;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getMK() {
        return MK;
    }

    public void setMK(String MK) {
        this.MK = MK;
    }
}
