package com.hw.jwt.controller;

import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public class test {
    private static String secret = "mystar";//密匙

    private static String getToken() {
        Key KEY = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS512.getJcaName());
        Map<String, Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("type", "1");
        String payload = "{\"user_id\":\"sdfa\", \"expire_time\":\"2018-08-23 00:00:00\"}";
        String compactJws = Jwts.builder().setHeader(stringObjectMap).setPayload(payload).signWith(SignatureAlgorithm.HS512, KEY).compact();
        System.out.println("token : " + compactJws);
        return compactJws;
    }

    private static String parseToken(String token) {
        Key KEY = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS512.getJcaName());
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token);
        JwsHeader header = claimsJws.getHeader();
        Claims body = claimsJws.getBody();
        return body.get("user_id") + "=====\n" + body.get("expire_time");
    }

    public static void main(String[] args) {
        String token = getToken();
        String info = parseToken(token);
        System.out.println(token + "\n" + info);
    }
}
