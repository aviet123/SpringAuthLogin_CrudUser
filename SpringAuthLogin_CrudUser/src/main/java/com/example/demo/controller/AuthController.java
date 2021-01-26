package com.example.demo.controller;

import com.example.demo.payload.LoginRequest;
import com.example.demo.payload.LoginResponse;
import com.example.demo.security.jwt.JwtTokenProvider;
import com.example.demo.security.service.CustomUserDetails;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        return ResponseEntity.ok(new LoginResponse(jwt));
    }

//    @GetMapping("/refreshtoken")
//    public ResponseEntity<?> refreshToken(HttpServletRequest httpServletRequest){
//        DefaultClaims claims = (DefaultClaims) httpServletRequest.getAttribute("claims");
//        Map<String, Object> expectMap = getMapFromIoJsonwebtokenClaims(claims);
//        String jwt = tokenProvider.doGenerateRefreshToken(expectMap,expectMap.get("sub").toString());
//        return ResponseEntity.ok(jwt);
//    }
//
//    private Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
//        Map<String, Object> expectMap = new HashMap<>();
//        for (Map.Entry<String, Object> entry: claims.entrySet()){
//            expectMap.put(entry.getKey(),entry.getValue());
//        }
//        return expectMap;
//    }


}
