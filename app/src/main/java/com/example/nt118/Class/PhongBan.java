package com.example.nt118.Class;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhongBan {
    private String MAPH;
    private String TRPH;
    private Date NGNC;

    public String getMAPH() {
        return MAPH;
    }

    public void setMAPH(String MAPH) {
        this.MAPH = MAPH;
    }

    public String getTRPH() {
        return TRPH;
    }

    public void setTRPH(String TRPH) {
        this.TRPH = TRPH;
    }

    public Date getNGNC() {
        return NGNC;
    }

    public void setNGNC(Date NGNC) {
        this.NGNC = NGNC;
    }

    public PhongBan() {
    }
    public static PhongBan convertJsonToPhongBan(String jsonStr) {
        PhongBan phongBan = new PhongBan();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);

            phongBan.setMAPH(jsonObject.optString("maph"));
            phongBan.setTRPH(jsonObject.optString("trph"));

            // Phân tích ngày nghiệp chức
            try {
                String ngNcStr = jsonObject.optString("ngnc");
                if (!ngNcStr.isEmpty()) {
                    Date ngNc = sdf.parse(ngNcStr);
                    phongBan.setNGNC(ngNc);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return phongBan;
    }
}
