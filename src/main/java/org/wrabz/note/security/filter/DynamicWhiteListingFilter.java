package org.wrabz.note.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//@Component
public class DynamicWhiteListingFilter extends OncePerRequestFilter {

    @Value("${whitelisted.ips}")
    private List<String> whiteListedIps;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String client = request.getRemoteAddr();
        if (!whiteListedIps.contains(client)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied from that location");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
