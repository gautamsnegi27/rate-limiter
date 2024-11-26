package com.blueoptima.ratelimiter.key.impl;

import com.blueoptima.ratelimiter.key.RateLimitKeyGenerator;

public class IPBasedKeyGenerator implements RateLimitKeyGenerator {

  @Override
  public String generateKey(String userId, String ipAddress) {
    return userId + KEY_SEPARATOR + ipAddress;
  }
}
