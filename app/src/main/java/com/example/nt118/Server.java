package com.example.nt118;

import android.os.AsyncTask;
import android.util.Log;

import com.example.nt118.Class.NhanVien;
import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractMap;
import java.util.Map;

public class Server {
    public interface PostResponseListener {
        void onPostCompleted(Map.Entry<String, Integer> response);
    }
    public void postAsync(final String apiUrl, final String postData, final PostResponseListener listener) {
        new AsyncTask<Void, Void, Map.Entry<String, Integer>>() {
            @Override
            protected Map.Entry<String, Integer> doInBackground(Void... voids) {
                return sendPostRequest(apiUrl,postData);
            }

            @Override
            protected void onPostExecute(Map.Entry<String, Integer> response) {
                // Gọi callback listener khi POST hoàn thành
                listener.onPostCompleted(response);
            }
        }.execute();
    }
    public static String hashPassword(String password) {
        try {
            // Khởi tạo đối tượng MessageDigest với thuật toán SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Chuyển đổi mật khẩu thành dãy byte
            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);

            // Băm mật khẩu và lấy kết quả dưới dạng mảng byte
            byte[] hashBytes = digest.digest(passwordBytes);

            // Chuyển đổi mảng byte thành chuỗi HEX
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static int Login(String usernname, String password){
        password = hashPassword(password);
        NhanVien nhanVien = new NhanVien();
        nhanVien.setMANV(usernname);
        nhanVien.setMK(password);

        String jsonString = new Gson().toJson(nhanVien);

        //Log.i("TestLogin", jsonString);
        //Log.i("TestLogin", sendPostRequest("https://s3.cazo-dev.net/NT118/api/Home/Login",jsonString));

        return -1;
    }

    public static Map.Entry<String, Integer> sendPostRequest(String apiUrl, String postData) {
        try {
            // Tạo URL từ địa chỉ API
            URL url = new URL(apiUrl);

            // Mở kết nối HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json"); // Thay đổi loại dữ liệu nếu cần

            // Cho phép ghi dữ liệu đến server
            connection.setDoOutput(true);

            // Ghi dữ liệu cần POST lên Output Stream
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            osw.write(postData);
            osw.flush();
            osw.close();

            // Đọc dữ liệu trả về từ server
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

                return new AbstractMap.SimpleEntry<>(response.toString(), responseCode);
            } else {
                return new AbstractMap.SimpleEntry<>("",responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new AbstractMap.SimpleEntry<>("Exception: " + e.getMessage(), -1);
        }
    }
}
