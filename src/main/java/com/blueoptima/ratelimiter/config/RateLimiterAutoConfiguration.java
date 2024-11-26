package com.blueoptima.ratelimiter.config;

import com.blueoptima.ratelimiter.aspect.RateLimiterAspect;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(RateLimiterProperties.class)
@Import({RateLimiterAspect.class})
public class RateLimiterAutoConfiguration {
}
