//package ru.denis.aestymes.jwts;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Configuration
//public class JwtFilterAfter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//        String jwtToken = null;
//
//        if(authHeader != null && authHeader.startsWith("Bearer ")) {
//            String cookie_jwt = getTokenFromCookies(request);
//
//            if(cookie_jwt != null) {
//                jwtToken = cookie_jwt;
//            } else {
//                filterChain.doFilter(request, response);
//                return;
//            }
//        } else {
//            jwtToken = authHeader.substring(7);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    public String getTokenFromCookies(HttpServletRequest request) {
//        if(request.getCookies() != null) {
//            for(Cookie cookie : request.getCookies()) {
//                if(cookie.getName().equals("JWT_TOKEN")) {
//                    return cookie.getValue();
//                }
//            }
//        }
//
//        return null;
//    }
//}
