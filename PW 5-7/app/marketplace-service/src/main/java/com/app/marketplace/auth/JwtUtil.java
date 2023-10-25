package com.app.marketplace.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JwtUtil {
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public Jws<Claims> getClaims(final String token) {
        return Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token);
    }

    public Set<String> getRolesFromRequest(HttpServletRequest request) {
        String token = request.getHeader("authorization").substring("Bearer ".length());
        String headerRoles = getClaims(token).getPayload().get("role").toString();

        Set<String> roles = new HashSet<>();

        Pattern pattern = Pattern.compile("authority=([A-Z]+)");
        Matcher matcher = pattern.matcher(headerRoles);

        while (matcher.find()) {
            roles.add(matcher.group(1));
        }
        return roles;
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

