package org.wrabz.note.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@Component
public class UserAgentFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null && userAgent.contains("Mozilla")){
            //Additional processing for requests from browser
            System.out.println("Request from browser");
        }
        filterChain.doFilter(request, response);
    }
}
