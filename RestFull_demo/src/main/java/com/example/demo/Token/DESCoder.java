package com.example.demo.Token;

import sun.misc.BASE64Decoder;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;

public class DESCoder {
    private Key toKey(byte[] key) throws Exception {
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DESBuilder.KEY_ALGORTHM);
        SecretKey secretKey = keyFactory.generateSecret(dks);
        return secretKey;
    }

    public Key toKey(String key) throws Exception {
        byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        Key keyObj = toKey(keyBytes);
        return keyObj;
    }
}
