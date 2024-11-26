package com.blueoptima.ratelimiter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class RateLimitExceededException extends RuntimeException {

  public RateLimitExceededException(String userId, String apiPath, int limit) {
    super(String.format("Rate limit exceeded for user %s on path %s. Limit: %d",
        userId, apiPath, limit));
  }
}
