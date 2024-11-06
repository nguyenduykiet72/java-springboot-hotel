//package com.example.javahotel.security;
//
//import com.example.javahotel.utils.JWTUtils;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class JWTAuthFilter extends OncePerRequestFilter {
//
//    private final JWTUtils jwtUtils;
//    private final UserDetailsService userDetailsService;
//
//    public JWTAuthFilter(JWTUtils jwtUtils, UserDetailsService userDetailsService) {
//        this.jwtUtils = jwtUtils;
//        this.userDetailsService = userDetailsService;
//    }
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        final String authHeader = request.getHeader("Authorization");
//        final String jwtToken;
//        final String userEmail;
//
//        if (authHeader == null || authHeader.isBlank()){
//            filterChain.doFilter(request,response);
//            return;
//        }
//
////        jwtToken = authHeader.subString(7);
//        jwtToken = authHeader.replace("Bearer ", "");
//        userEmail = jwtUtils.extractUsername(jwtToken);
//        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
//            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
//            if (jwtUtils.isValidToken(jwtToken,userDetails)){
//                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
//                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.setContext(securityContext);
//            }
//        }
//        filterChain.doFilter(request,response);
//    }
//}
