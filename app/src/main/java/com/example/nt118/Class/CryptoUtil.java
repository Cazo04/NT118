package com.example.nt118.Class;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import android.util.Base64;

import java.io.UnsupportedEncodingException;

public class CryptoUtil {
    private static final String AES = "AES";
    private static final String AES_CIPHER = "AES/CBC/PKCS5PADDING";
    private static final String CHARSET = "UTF-8";

    // Khóa AES 16 byte (Bạn cần tạo một khóa bảo mật hơn)
    private static final byte[] KEY; // Khóa 16 byte

    static {
        try {
            KEY = "qdVXgyEik4nMjwGt".getBytes(CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static final byte[] IV;  // IV 16 byte

    static {
        try {
            IV = "AXMiBS9PDkXgb3tS".getBytes(CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(IV);
            SecretKeySpec skeySpec = new SecretKeySpec(KEY, AES);

            Cipher cipher = Cipher.getInstance(AES_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(IV);
            SecretKeySpec skeySpec = new SecretKeySpec(KEY, AES);

            Cipher cipher = Cipher.getInstance(AES_CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
