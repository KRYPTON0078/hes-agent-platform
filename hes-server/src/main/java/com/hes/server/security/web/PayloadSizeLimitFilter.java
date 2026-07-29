package com.hes.server.security.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Reject oversized Agent/ops JSON bodies early (OWASP input size hardening).
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 2)
public class PayloadSizeLimitFilter extends OncePerRequestFilter {

    public static final int MAX_BYTES = 64 * 1024;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        int length = request.getContentLength();
        if (length > MAX_BYTES) {
            response.setStatus(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
            response.setContentType("application/json");
            response.getWriter().write("{\"code\":\"VALIDATION_FAILED\",\"message\":\"payload too large\"}");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
