package com.spring.todomanagement.common.util;

import com.spring.todomanagement.auth.config.WebConfig;
import com.spring.todomanagement.auth.support.AuthArgumentResolver;
import com.spring.todomanagement.auth.support.JwtUtil;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ExternalConfig {

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }

    @Bean
    public WebConfig webConfig() {
        return new WebConfig(new AuthArgumentResolver(jwtUtil()));
    }
}
