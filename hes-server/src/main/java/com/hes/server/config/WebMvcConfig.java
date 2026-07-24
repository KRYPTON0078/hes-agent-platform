package com.hes.server.config;

import com.hes.server.security.AgentAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AgentAuthInterceptor agentAuthInterceptor;

    public WebMvcConfig(AgentAuthInterceptor agentAuthInterceptor) {
        this.agentAuthInterceptor = agentAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(agentAuthInterceptor)
                .addPathPatterns("/api/v1/agent/**");
    }
}
