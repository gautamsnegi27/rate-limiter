package com.blueoptima.ratelimiter.controller;

import com.blueoptima.ratelimiter.annotation.RateLimit;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RateLimiterController {

  @RateLimit
  @GetMapping(value = "/developers", produces = MediaType.APPLICATION_JSON_VALUE)
  public String getDevelopers() {
    //actual implementation may vary
    return """
        { "response": "List of Developers" }
        """;
  }

  @RateLimit
  @GetMapping(value = "/organizations", produces = MediaType.APPLICATION_JSON_VALUE)
  public String getOrganizations() {
    //actual implementation may vary
    return """
        { "response": "List of Organizations" }
        """;
  }
}
