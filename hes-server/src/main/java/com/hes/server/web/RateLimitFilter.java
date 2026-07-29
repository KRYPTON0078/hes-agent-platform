package com.hes.server.web;

import com.hes.server.security.ratelimit.RateLimitService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Rate-limits Agent message ingest using Redis when available, else in-memory.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class RateLimitFilter implements Filter {

    private final RateLimitService rateLimitService;

    public RateLimitFilter(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest http = (HttpServletRequest) request;
        if (!http.getRequestURI().startsWith("/api/v1/agent/messages")) {
            chain.doFilter(request, response);
            return;
        }
        String key = "agent:" + http.getRemoteAddr();
        if (!rateLimitService.allow(key)) {
            HttpServletResponse httpResp = (HttpServletResponse) response;
            httpResp.setStatus(429);
            httpResp.setContentType("application/json");
            httpResp.getWriter().write("{\"code\":\"VALIDATION_FAILED\",\"message\":\"rate limit exceeded\"}");
            return;
        }
        chain.doFilter(request, response);
    }
}
