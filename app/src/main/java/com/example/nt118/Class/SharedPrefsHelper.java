package com.example.nt118.Class;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {

    private static final String PREFS_NAME = "LoginData";
    private SharedPreferences sharedPreferences;

    public SharedPrefsHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveUsernameAndPassword(String username, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", CryptoUtil.encrypt(username));
        editor.putString("password", CryptoUtil.encrypt(password));
        editor.apply();
    }
    public void clearData(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public String getUsername() {
        String encryptedUsername = sharedPreferences.getString("username", null);
        return CryptoUtil.decrypt(encryptedUsername);
    }

    public String getPassword() {
        String encryptedPassword = sharedPreferences.getString("password", null);
        return CryptoUtil.decrypt(encryptedPassword);
    }

}