package com.example.documentconvertservice.security;

import com.example.documentconvertservice.data.User;
import com.example.documentconvertservice.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JWTFilter extends OncePerRequestFilter {

    private final UserService userService;


    private final JWTUtil jwtUtil;


    public JWTFilter(UserService userService, JWTUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String token = extractAuthorizationHeader(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final String tokenUser = jwtUtil.extractUsername(token);

        if (tokenUser != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final UserDetails user = userService.loadUserByUsername(tokenUser);

            if (!jwtUtil.isTokenValid(token, tokenUser)) {
                throw new UsernameNotFoundException("Failed to authenticate!");
            }

            final SecurityContext context = SecurityContextHolder.createEmptyContext();
            final UsernamePasswordAuthenticationToken authToken
                    = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);
        }

        filterChain.doFilter(request, response);
    }

    private String extractAuthorizationHeader(HttpServletRequest request) {
        String AUTH_HEADER = "Authorization";
        final String headerValue = request.getHeader(AUTH_HEADER);

        String AUTH_TYPE = "Bearer";
        if (headerValue == null || !headerValue.startsWith(AUTH_TYPE)) {
            return null;
        }

        return headerValue.substring(AUTH_TYPE.length()).trim();
    }
}
