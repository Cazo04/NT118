package com.example.nt118.Class;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationManagerData {
    private int Id;
    private String Phban;
    private String Date;
    private String Content;
    private int Received;
    private int Seen;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getPhban() {
        return Phban;
    }

    public void setPhban(String phban) {
        Phban = phban;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getReceived() {
        return Received;
    }

    public void setReceived(int received) {
        Received = received;
    }

    public int getSeen() {
        return Seen;
    }

    public void setSeen(int seen) {
        Seen = seen;
    }
    public static NotificationManagerData convertJsonToNotificationManagerData(String jsonStr) {
        NotificationManagerData notificationManagerData = new NotificationManagerData();

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);

            notificationManagerData.setId(jsonObject.optInt("id"));
            notificationManagerData.setPhban(jsonObject.optString("phban"));
            notificationManagerData.setDate(jsonObject.optString("date"));
            notificationManagerData.setContent(jsonObject.optString("content"));
            notificationManagerData.setReceived(jsonObject.optInt("received"));
            notificationManagerData.setSeen(jsonObject.optInt("seen"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return notificationManagerData;
    }

    public static List<NotificationManagerData> convertJsonToList(String jsonStr) {
        JsonArray jsonArray = JsonParser.parseString(jsonStr).getAsJsonArray();
        List<NotificationManagerData> res = new ArrayList<>();

        for (JsonElement element : jsonArray) {
            String individualJsonString = element.toString();
            NotificationManagerData data = convertJsonToNotificationManagerData(individualJsonString);
            res.add(data);
        }
        return res;
    }
}
