package com.mor.microservices.currencyexchangeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CircuitBreakerController {
	
	private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);
	
	/*
	 * Spring Retry provides an ability to automatically re-invoke a failed operation. 
	 * This is helpful where the errors may be transient (like a momentary network glitch).
	 * 
	 * The fallback method can provide some default value or behavior for the remote call that was not permitted.
	 */
	@GetMapping("/sample-api")
	//@Retry(name="default")
	@Retry(name="sample-api", fallbackMethod="hardCodedResponse")
	public String sampleApi() {
		logger.info("Sample api call received");
		ResponseEntity<String> entity = new RestTemplate()
				.getForEntity("http://localhost:8001/wrongUrl", String.class);
		return entity.getBody();
	}
	
	/*
	 * Spring Cloud Circuit Breaker watches for failing calls to that method, 
	 * and if failures build up to a threshold, 
	 * Spring Cloud Circuit Breaker opens the circuit so that subsequent calls automatically fail. 
	 * While the circuit is open, Spring Cloud Circuit Breaker redirects calls to the method, 
	 * and they’re passed on to our specified fallback method.
	 * 
	 * RateLimiter: setting a limit on how many requests a consumer is allowed to make in a given unit of time. 
	 * We reject any requests above the limit with an appropriate response
	 * 
	 * Bulkheads: set a limit on the number of concurrent calls we make to a remote service. 
	 * We treat calls to different remote services as different, 
	 * isolated pools and set a limit on how many calls can be made concurrently.
	 */
	@GetMapping("/sample-api-circuitbreaker")
	@CircuitBreaker(name="default", fallbackMethod="hardCodedResponse")
	@RateLimiter(name="default")
	@Bulkhead(name="default")
	public String sampleApiCircuitBreaker() {
		logger.info("Sample api call received");
		ResponseEntity<String> entity = new RestTemplate()
				.getForEntity("http://localhost:8001/wrongUrl", String.class);
		return entity.getBody();
	}
	
	public String hardCodedResponse(Exception ex ) {
		return "fallback response";
	}

}
