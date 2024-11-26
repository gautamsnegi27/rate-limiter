package com.blueoptima.ratelimiter.key;

public interface RateLimitKeyGenerator {
  String KEY_SEPARATOR = "_";

  String generateKey(String userId, String keyGenerator);

}
