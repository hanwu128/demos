package com.example.demo.Token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.util.*;

public class JWT {
    /**
     * 利用JWT生成Token信息
     *
     * @param claims 一个Map
     * @param secret 用于进行签名的密匙
     */
    public static String generateToken(Map<String, Object> claims, String secret) throws Exception {
        DESCoder desCoder = new DESCoder();
        Key key = desCoder.toKey(secret);
        //设置过期时间为10分钟
        Date ecpiration = new Date(System.currentTimeMillis() + 10*60*1000L);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(ecpiration)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    /**
     * 利用jwt解析token
     *
     * @param token  解析的Token信息
     * @param secret 用于进行签名的密匙
     */
    public static Optional<Claims> getClaimsFromToken(String token, String secret) throws Exception {
        Claims claims;
        DESCoder desCoder = new DESCoder();
        Key key = desCoder.toKey(secret);
        try {
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();
            return Optional.of(claims);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * 验证Token是否过期
     */
    public static boolean isExprired(String token, String secret) throws Exception {
        Optional<Claims> claim = getClaimsFromToken(token, secret);
        if (claim.isPresent()) {
            Date expiration = claim.get().getExpiration();
            return expiration.before(expiration);
        }
        return false;
    }

    /**
     * 获取token中的参数值
     */
    public static Map<String, Object> extractInfo(String token, String secret) throws Exception {
        Optional<Claims> claim = getClaimsFromToken(token, secret);
        if (claim.isPresent()) {
            Map<String, Object> info = new HashMap<>();
            Set<String> keySet = claim.get().keySet();
            //通过迭代提取token中的字符串
            Iterable<String> iterable = (Iterable<String>) keySet.iterator();
            while (((Iterator) iterable).hasNext()) {
                String key = (String) ((Iterator) iterable).next();
                Object value = claim.get().get(key);
                info.put(key, value);
            }
            return info;

        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> claim = new HashMap<>();
        claim.put("alg", "HS256");
        claim.put("typ", "JWT");
        claim.put("user_id",10L);
        String secret = "JKKLJOoasdlfj";
        String token = generateToken(claim, secret);
        Thread.sleep(11*1000);
        boolean b = isExprired(token,secret);
        System.out.println(token+"================================"+b);

    }


}
