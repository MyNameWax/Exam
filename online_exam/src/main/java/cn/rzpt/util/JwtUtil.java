package cn.rzpt.util;

import cn.rzpt.common.context.BaseContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;


@Component
public class JwtUtil {



    public static String createJWT(String secretKey, long ttlMillis, Map<String, Long> claims) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime + "ms");
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                .setExpiration(exp);
        return builder.compact();
    }

    public static Claims parseJWT(String secretKey, String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token).getBody();
            Object value = claims.get("id");
            BaseContext.setCurrentId(value.toString());
            return claims;
        } catch (Exception e) {
            throw new RuntimeException("请先登录");
        }
    }

}
