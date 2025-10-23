package com.wqz.echonetwork.utils;

import com.wqz.echonetwork.entity.po.User;
import com.wqz.echonetwork.entity.vo.Result;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.20
 */
public class JwtUtil {
    private static final SecretKey SECRET_KEY;
    private static final long EXPIRATION_TIME;


    static {
        YamlLoader yamlLoader = new YamlLoader("application.yml");
        String secret = yamlLoader.getString("app.jwt.secret");
        SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        EXPIRATION_TIME = (long) yamlLoader.getInt("app.jwt.expiration") * 24 * 3600 * 1000;
    }

    public static String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public static Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", Long.class);
    }

    public static Integer getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", Integer.class);
    }

    public static Date getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }

    public static Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static void addToBlacklist(String token) {
        try {
            Claims claims = JwtUtil.getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            long ttl = expiration.getTime() - System.currentTimeMillis();

            if (ttl > 0) {
                RedisUtil.set(ConstField.JWT_BLACK_LIST + token,
                        (int) ttl / 1000,
                        "logout "
                );
            }
        } catch (Exception e) {
            System.err.println("将 Token 加入黑名单失败：" + e.getMessage());
        }
    }

    public static boolean isTokenBlacklisted(String token) {
        return RedisUtil.exists(ConstField.JWT_BLACK_LIST + token);
    }

    public static boolean isTokenValid(String token) {
        try {
            // 先检查黑名单
            if (isTokenBlacklisted(token)) {
                return false;
            }
            return validateToken(token);
        } catch (Exception e) {
            return false;
        }
    }

    public static Long getCurrentUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7);
        return JwtUtil.getUserIdFromToken(token);
    }
}
