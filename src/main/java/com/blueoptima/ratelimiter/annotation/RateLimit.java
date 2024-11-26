package com.blueoptima.ratelimiter.annotation;

import com.blueoptima.ratelimiter.key.impl.DefaultKeyGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimit {
  int limit() default 100; // Default limit
  int duration() default 1; // Default duration
  TimeUnit timeUnit() default TimeUnit.HOURS; // Default time unit

  // Optional custom key generator if default user/API path doesn't suit
  Class<?> keyGenerator() default DefaultKeyGenerator.class;
}
