package co.com.powerup.user.api.security.jwt;

import co.com.powerup.user.model.user.UserModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JwtProvider {

    private final SecretKey jwtSecretKey;
    private final ObjectMapper objectMapper;

    public JwtProvider(@Value("${JWT_AUTH_SECRET}") String jwtSecret, @Autowired ObjectMapper objectMapper) {
        this.jwtSecretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.objectMapper = objectMapper;

    }

    public String generateToken(Authentication authentication, UserModel user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", authentication.getAuthorities());
        claims.put("document", user.getDocumentNumber());


        return Jwts.builder()
                .claims(claims)
                .subject(authentication.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(15)))
                .signWith(jwtSecretKey)
                .compact();

    }


    public Boolean validTToken(String token) {
        try {
            getBody(token);
            return true;
        } catch (JwtException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public Claims getBody(String token) {
        return Jwts.parser()
                .verifyWith(jwtSecretKey).build()
                .parseSignedClaims(token.replace("Bearer ", "")).getPayload();
    }

    public Collection<SimpleGrantedAuthority> getRoles(Claims claims) throws JsonProcessingException {
        List<Map<String, String>> roles = objectMapper.readValue(
                objectMapper.writeValueAsString(claims.get("role")),
                new TypeReference<List<Map<String, String>>>() {
                }
        );
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.get("authority")))
                .toList();
    }

    public String getDocumentNumber(Claims claims) {
        return (String) claims.get("document");
    }


}
