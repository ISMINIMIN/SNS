package minzdev.sns.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtils {

    private final Key key;

    @Value("${jwt.accessTokenExpiredTimeMs}")
    private long accessTokenExpiredTimeMs;

    public JwtTokenUtils(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String username) {
        Date now = new Date(System.currentTimeMillis());

        return Jwts.builder()
                .claim("username", username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenExpiredTimeMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

}
