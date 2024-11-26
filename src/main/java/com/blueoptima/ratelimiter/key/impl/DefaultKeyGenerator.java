package com.blueoptima.ratelimiter.key.impl;

import com.blueoptima.ratelimiter.key.RateLimitKeyGenerator;

public class DefaultKeyGenerator implements RateLimitKeyGenerator {

  private static final String API_SEPARATOR = "/";

  @Override
  public String generateKey(String userId, String apiPath) {
    return userId.concat(KEY_SEPARATOR).concat(apiPath.replace(API_SEPARATOR, KEY_SEPARATOR));
  }
}
