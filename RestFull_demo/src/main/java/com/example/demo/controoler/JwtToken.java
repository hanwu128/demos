package com.example.demo.controoler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sun.org.apache.xpath.internal.Arg;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtToken {

    /**
     * token密匙
     */
    public static final String SECRET = "JKKLJOoasdlfj";

    /**
     * token 过期时间 10天
     */
    public static final int calendarFiedld = Calendar.DATE;
    public static final int calendarInterval = 10;

    /**
     * JWT 生成 Token
     * Token构成 header payload signature
     * 登录成功后用户名user_id，参数user_id不可为空
     */
    public static String createToken(Long user_id) throws Exception {
        Date iatDate = new Date();

        Calendar nowTime = Calendar.getInstance();
        nowTime.add(calendarFiedld, calendarInterval);

        Date expiresDate = nowTime.getTime();

        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        String token = JWT.create().withHeader(map)
                .withClaim("iss", "service")
                .withClaim("aud", "APP")
                .withClaim("user_id", null == user_id ? null : user_id.toString())
                .withIssuedAt(iatDate)
                .withExpiresAt(expiresDate)
                .sign(Algorithm.HMAC256(SECRET));

        return token;
    }

    /**
     * 解密Token
     */
    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            //Token校验失败，抛出Token验证非法异常
            e.printStackTrace();
        }
        return jwt.getClaims();
    }

    /**
     * 根据Token 获取user_id
     */
    public static Long getAppUID(String token) {
        Map<String, Claim> claims = verifyToken(token);
        Claim user_id_claim = claims.get("user_id");
        if ((null == user_id_claim) || StringUtils.isEmpty(user_id_claim.asString())) {
            System.out.println("token 校验失败");
        }
        return Long.valueOf(user_id_claim.asString());
    }

    public static void main(String[] args) throws Exception {
        Long user_id = 10L;
        String token2 = createToken(user_id);
        Map<String, Claim> mapww = verifyToken(token2);
        Long useid2 = getAppUID(token2);

        System.out.println(token2+"+++++++++++++++++++"+mapww+"================"+useid2);
    }

}
