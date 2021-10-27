package com.mor.microservices.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/*
 * @Component: Indicates that an annotated class is a "component". 
 * Such classes are considered as candidates for auto-detection 
 * when using annotation-based configuration and classpath scanning.
 */
@Component
public class LoggingFilter implements GlobalFilter {

	private Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
	
	// Global filters are executed for every route defined in the API Gateway. 
	// The main difference between pre-filter and post-filter class is that the pre-filter code is executed 
	// before Spring Cloud API Gateway routes the request to a destination web service endpoint. 
	// While the post-filter code will be executed after Spring Cloud API Gateway 
	// has routed the HTTP request to a destination web service endpoint.
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		logger.info("Path is: {}", exchange.getRequest().getPath());
		return chain.filter(exchange);
	}

}
