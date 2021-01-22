package com.johncerpa.currencyexchangemicroservice;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class CircuitBreakerController {

   private final Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

   @GetMapping("/sample-api")
   //@Retry(name = "sample-api", fallbackMethod = "ownFallbackMethod")
   @CircuitBreaker(name = "default", fallbackMethod = "ownFallbackMethod")
   public String sampleAPI() {
      logger.info("Sample API received a request");

      final ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:9123/dummy-api", String.class);

      return forEntity.getBody();
   }

   public String ownFallbackMethod(Exception ex) {
      return "Our own fallback response";
   }

   @GetMapping("/rate-limited-endpoint")
   @RateLimiter(name = "default")
   public String endpointWithRateLimiter() {
      return "Testing rate limiter endpoint";
   }

}
