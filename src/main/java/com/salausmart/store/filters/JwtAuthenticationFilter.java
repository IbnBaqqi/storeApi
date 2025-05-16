package com.salausmart.store.filters;

import com.salausmart.store.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        var token = authHeader.replace("Bearer ", "");
        if (!jwtService.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

//        tell spring user is authenticated and allow access to protected resources
        var authentication = new UsernamePasswordAuthenticationToken( jwtService.getUserIdFromToken(token), null, null );

//        attaching additional metadata about the request like IPAddress & other stuff to the authentication object
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

//        stores information about the currently authenticated user (in future to get access to current user )
        SecurityContextHolder.getContext().setAuthentication(authentication);

//        Pass control to the next user in the filter chain
        filterChain.doFilter(request, response);

    }
}
