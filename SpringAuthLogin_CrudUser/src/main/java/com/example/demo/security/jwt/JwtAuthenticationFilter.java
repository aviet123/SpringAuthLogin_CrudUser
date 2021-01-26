package com.example.demo.security.jwt;

import com.example.demo.security.service.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getTokenFromRequest(httpServletRequest);

            if (jwt == null && jwtTokenProvider.validateToken(jwt)) {
                String username = jwtTokenProvider.getUserIdFromToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken
                            authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ExpiredJwtException ex){
//            String isRefreshToken = httpServletRequest.getHeader("isRefreshToken");
//            String requestURL = httpServletRequest.getRequestURL().toString();
//
//            if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refreshtoken")){
//                allowForRefreshToken(ex, httpServletRequest);
//            }
        } catch (Exception ex){
            log.error("failed to set user authentication", ex);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

//    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest httpServletRequest) {
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
//                = new UsernamePasswordAuthenticationToken(null, null, null);
//        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//        httpServletRequest.setAttribute("claims",ex.getClaims());
//    }

    private String getTokenFromRequest(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
