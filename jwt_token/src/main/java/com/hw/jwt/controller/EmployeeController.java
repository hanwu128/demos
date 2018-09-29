package com.hw.jwt.controller;

import io.jsonwebtoken.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {

    @GetMapping(value = "/greeting")
    public String greeting() {
        return "Hello world!";
    }


    public static void main(String[] args) {
        String message = "1234";
        String hash = "";
        String secret = "mystar";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
            System.out.println(message + "#####" + hash+"\n"+"8a2OEnwiTHH5tIhkjhRuKVKOYN2jH5p3gOjDPdnLg0c=");
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}
