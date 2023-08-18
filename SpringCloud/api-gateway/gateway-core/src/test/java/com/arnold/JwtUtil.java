package com.arnold;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.compression.CompressionCodecs;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class JwtUtil {
 
    //过期时间(半小时)
    private static long tokenExpiration = 30*60*1000;
    //签名秘钥
    private static String tokenSignKey = "wodexiaokeai";
 
    //根据参数生成token，三部分组成，用 . 隔开
    public static String createToken(Long userId, String userName) {

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("userName", userName);
        String token = JWTUtil.createToken(payload, tokenSignKey.getBytes());
//        String token = Jwts.builder()
//                .setSubject(userName)
//                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration)) // 公共部分结束
//                .claim("jti", UUID.randomUUID().toString())
//                .claim("userId", userId)
//                .claim("userName", userName) // 私有部分，实际上真正需要封装的信息（id和name）
//                .claim("iat", new Date())
//                .claim("iss", "dt360.com")
//                .claim("nbf", new Date())
//                .signWith(SignatureAlgorithm.HS512, new SecretKeySpec(tokenSignKey.getBytes(), SignatureAlgorithm.HS512.getJcaName())) // 签名部分
//                .compressWith(CompressionCodecs.DEFLATE)
//                .compact();
        return token;
    }
 
    //根据token字符串得到用户id
    public static Long getUserId(String token) {
        if(StringUtils.isEmpty(token)) return null;
 
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        Integer userId = (Integer)claims.get("userId");
        return userId.longValue();
    }
 
    //根据token字符串得到用户名称
    public static String getUserName(String token) {
        if(StringUtils.isEmpty(token)) return "";
 
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String)claims.get("userName");
    }
 
    // 测试
    public static void main(String[] args) {
        String token = JwtUtil.createToken(1L, "lucy");

        System.out.println(JWTUtil.verify(token, "tokenSignKey".getBytes()));

        JWT jwt = JWTUtil.parseToken(token);
        System.out.println(jwt.getPayload("userId"));
        System.out.println(jwt.getPayload("userName"));

//        System.out.println(token);
//        System.out.println(JwtUtil.getUserId(token));
//        System.out.println(JwtUtil.getUserName(token));
    }
}