
package cn.rzpt.util;

import cn.rzpt.common.context.BaseContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;


@Component
public class SysUserJwtUtil {


    public String createJWT(String secretKey, long ttlMillis, Map<String, String> claims) {
        // 将密钥转换为Key对象
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // 计算过期时间
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        // 构建JWT
        return Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(exp)
                .compact();
    }


    public Claims parseJWT(String secretKey, String token) {
        try {
            // 将密钥转换为Key对象
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

            // 解析JWT
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            Claims claims = claimsJws.getBody();

            // 获取并设置用户ID
            String id = claims.get("id", String.class);
            if (id != null) {
                BaseContext.setCurrentId(id);
            }

            return claims;
        } catch (Exception e) {
            throw new RuntimeException("登录超时,请重新登录");
        }
    }

}
