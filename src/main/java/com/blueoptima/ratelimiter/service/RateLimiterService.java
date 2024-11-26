package com.blueoptima.ratelimiter.service;

import com.blueoptima.ratelimiter.config.RateLimiterProperties;
import com.blueoptima.ratelimiter.exception.RateLimitExceededException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class RateLimiterService {
  private final RateLimiterProperties properties;
  private final Map<String, RequestTracker> requestTrackers = new ConcurrentHashMap<>();

  public RateLimiterService(RateLimiterProperties properties) {
    this.properties = properties;
  }

  public void allowRequest(String key, String apiPath) {
    log.info("got request for key: {}", key);
    RateLimiterProperties.ApiRateLimit config = getEffectiveRateLimit(key);

    RequestTracker tracker = requestTrackers.computeIfAbsent(key, k -> new RequestTracker());
    boolean allowed = tracker.recordRequest(config.getLimit(), config.getWindowSeconds());

    if (!allowed) {
      log.error("user: {}, exhausted the limit of: {}, request per second: {}", key,
          config.getLimit(), config.getWindowSeconds());

      throw new RateLimitExceededException(key, apiPath, config.getLimit());
    }
  }

  private RateLimiterProperties.ApiRateLimit getEffectiveRateLimit(String key) {
    // Check for specific user-api combination limit
    if (properties.getLimits().containsKey(key)) {
      return properties.getLimits().get(key);
    }

    // Return default if no specific limit found
    RateLimiterProperties.ApiRateLimit defaultLimit = new RateLimiterProperties.ApiRateLimit();
    defaultLimit.setLimit(properties.getDefaultLimit());
    defaultLimit.setWindowSeconds(properties.getDefaultWindowSeconds());
    return defaultLimit;
  }

  private static class RequestTracker {
    private final Map<Long, AtomicInteger> requestCountByTimestamp = new ConcurrentHashMap<>();

    synchronized boolean recordRequest(int limit, int windowSeconds) {
      long currentTimestamp = Instant.now().getEpochSecond();
      long windowStart = currentTimestamp - windowSeconds;

      // Remove outdated timestamps
      requestCountByTimestamp.entrySet().removeIf(entry -> entry.getKey() < windowStart);

      // Count requests in current window
      int totalRequestsInWindow =
          requestCountByTimestamp.entrySet().stream().filter(entry -> entry.getKey() >= windowStart)
              .mapToInt(entry -> entry.getValue().get()).sum();

      if (totalRequestsInWindow >= limit) {
        return false; // Rate limit exceeded
      }

      // Increment request count for current timestamp
      requestCountByTimestamp.computeIfAbsent(currentTimestamp, k -> new AtomicInteger(0))
          .incrementAndGet();

      return true;
    }
  }
}
