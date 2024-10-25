package com.example.orange.configurations.authentication.config;

import com.example.orange.common.exceptions.UnauthorizedException;
import com.example.orange.configurations.authentication.services.JwtService;
import com.example.orange.entites.Role;
import com.example.orange.entites.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.orange.configurations.authentication.config.WhiteList.WHITELIST;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();

        if (isWhitelisted(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return ;
        }

        String jwt = authHeader.substring(7);
        String username = jwtService.extractClaims(jwt).getSubject();
        List<String> roles = jwtService.extractClaims(jwt).get("roles", List.class);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User userDetails = (User) userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, roles.stream().map(role -> new Role(role)).collect(Collectors.toList())
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }
    private boolean isWhitelisted(String requestURI) {
        return WHITELIST.stream().anyMatch(requestURI::equals);
    }
}