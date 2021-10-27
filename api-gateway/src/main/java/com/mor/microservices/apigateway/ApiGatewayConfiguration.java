package com.mor.microservices.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * @Configuration: Indicates that a class declares one or more @Bean methods 
 * and may be processed by the Spring container to generate bean definitions 
 * and service requests for those beans at runtime
 */
@Configuration
public class ApiGatewayConfiguration {

	// Api Gateway handles route resolution
	@Bean
	public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
		// If url starts with /currency-exchange/ then lookup naming server(Eureka) 
		// and look for CURRENCY-EXCHANGE-SERVICE and with the load balancers
		return builder.routes()
				.route(p -> p.path("/currency-exchange/**")
						.uri("lb://CURRENCY-EXCHANGE-SERVICE"))
				.build();
	}
	
}
