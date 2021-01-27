package com.example.demo.security.jwt;

import com.example.demo.security.service.CustomUserDetails;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {

    private final String JWT_SECRET = "viet";

    private final int JWT_EXPIRATION = 0;

    private final int REFRESH_EXPIRATION_DATE = 9000000;


    public String generateToken(CustomUserDetails userDetails){
        return Jwts.builder()
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                    .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                    .compact();
    }

    public String getUsernameFromToken(String token){
        Claims claims = Jwts.parser()
                            .setSigningKey(JWT_SECRET)
                            .parseClaimsJws(token)
                            .getBody();
        return claims.getSubject();
    }

    public String doGenerateRefreshToken(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+REFRESH_EXPIRATION_DATE))
                .signWith(SignatureAlgorithm.HS256,JWT_SECRET)
                .compact();
    }

    public boolean validateToken(String authToken){
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        }catch (MalformedJwtException ex){
            log.error("Invalid JWT token");
        }catch (ExpiredJwtException ex){
            log.error("Expired JWT token");
        }catch (UnsupportedJwtException ex){
            log.error("Unsupported JWT token");
        }catch (IllegalArgumentException ex){
            log.error("JWT claims string is empty");
        }
        return false;
    }

}
