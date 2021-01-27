package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.payload.LoginRequest;
import com.example.demo.payload.LoginResponse;
import com.example.demo.security.jwt.JwtTokenProvider;
import com.example.demo.security.service.CustomUserDetails;
import com.example.demo.security.service.UserDetailsServiceImpl;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

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
        return ResponseEntity.ok(jwt);
    }

    @GetMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(){
       String username = SecurityContextHolder.getContext().getAuthentication().getName();
       UserDetails user = userDetailsService.loadUserByUsername(username);
       String jwt = tokenProvider.doGenerateRefreshToken(user);
        return ResponseEntity.ok(jwt);
    }
//
//    private Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
//        Map<String, Object> expectMap = new HashMap<>();
//        for (Map.Entry<String, Object> entry: claims.entrySet()){
//            expectMap.put(entry.getKey(),entry.getValue());
//        }
//        return expectMap;
//    }


}
