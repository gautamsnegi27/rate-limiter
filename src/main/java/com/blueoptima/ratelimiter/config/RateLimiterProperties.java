package com.blueoptima.ratelimiter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@ConfigurationProperties(prefix = "app.rate-limiter")
public class RateLimiterProperties {

  private int defaultLimit = 100;
  private int defaultWindowSeconds = 3600;
  private Map<String, ApiRateLimit> limits = new HashMap<>();

  @Setter
  @Getter
  public static class ApiRateLimit {
    private int limit;
    private int windowSeconds;
  }

}
