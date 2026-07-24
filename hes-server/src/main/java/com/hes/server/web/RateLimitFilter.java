package com.hes.server.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple in-memory rate limit for Agent message ingest (demo-grade).
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class RateLimitFilter implements Filter {
    private static final int LIMIT = 120;
    private final Map<String, Window> windows = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest http = (HttpServletRequest) request;
        if (!http.getRequestURI().startsWith("/api/v1/agent/messages")) {
            chain.doFilter(request, response);
            return;
        }
        String key = http.getRemoteAddr();
        Window window = windows.computeIfAbsent(key, k -> new Window());
        if (!window.allow()) {
            HttpServletResponse httpResp = (HttpServletResponse) response;
            httpResp.setStatus(429);
            httpResp.getWriter().write("{\"code\":\"VALIDATION_FAILED\",\"message\":\"rate limit exceeded\"}");
            return;
        }
        chain.doFilter(request, response);
    }

    private static final class Window {
        private volatile long start = Instant.now().getEpochSecond();
        private final AtomicInteger count = new AtomicInteger();

        synchronized boolean allow() {
            long now = Instant.now().getEpochSecond();
            if (now - start >= 60) {
                start = now;
                count.set(0);
            }
            return count.incrementAndGet() <= LIMIT;
        }
    }
}
