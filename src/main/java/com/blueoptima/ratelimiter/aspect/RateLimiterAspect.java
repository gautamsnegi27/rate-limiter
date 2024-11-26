package com.blueoptima.ratelimiter.aspect;

import com.blueoptima.ratelimiter.annotation.RateLimit;
import com.blueoptima.ratelimiter.key.RateLimitKeyGenerator;
import com.blueoptima.ratelimiter.key.impl.DefaultKeyGenerator;
import com.blueoptima.ratelimiter.service.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Slf4j
@Aspect
@Component
public class RateLimiterAspect {

  private static final String USER_ID_HEADER = "user-id";
  private static final String ANONYMOUS = "anonymous";

  private final RateLimiterService rateLimiterService;

  public RateLimiterAspect(RateLimiterService rateLimiterService) {
    this.rateLimiterService = rateLimiterService;
  }

  @Around("@annotation(rateLimit)")
  public Object checkRateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit)
      throws Throwable {
    // Get current request and user details
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

    // Extract userId from header
    String userId = extractUserIdFromHeader(request);
    String apiPath = request.getRequestURI();

    // Determine key generator
    RateLimitKeyGenerator keyGenerator = getKeyGenerator(rateLimit);

    // Generate specific key if custom generator is provided
    String effectiveKey = keyGenerator.generateKey(userId, apiPath);

    // Check rate limit
    rateLimiterService.allowRequest(effectiveKey, apiPath);

    // Proceed with method execution
    return joinPoint.proceed();
  }

  private String extractUserIdFromHeader(HttpServletRequest request) {
    // Extract userId from header, with fallback to anonymous
    String userId = request.getHeader(USER_ID_HEADER);
    return Objects.nonNull(userId) ? userId : ANONYMOUS;
  }

  private RateLimitKeyGenerator getKeyGenerator(RateLimit rateLimit) {
    try {
      return (RateLimitKeyGenerator) rateLimit.keyGenerator().getDeclaredConstructor()
          .newInstance();
    } catch (Exception e) {
      log.error("while getKeyGenerator with rateLimit: {}, got exception", rateLimit, e);
      return new DefaultKeyGenerator();
    }
  }
}
