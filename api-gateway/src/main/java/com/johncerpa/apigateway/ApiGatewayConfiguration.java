package com.johncerpa.apigateway;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ApiGatewayConfiguration {
    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        Function<PredicateSpec, Buildable<Route>> routeFunction = p -> p.path("/get")
                .filters(f -> f.addRequestHeader("MyHeader", "MyURI"))
                .uri("http://httpbin.org:80");

        Function<PredicateSpec, Buildable<Route>> fullRewrite = p -> p.path("/currency-conversion-new/**")
                .filters(f -> f.rewritePath("/currency-conversion-new/(?<segment>.*)", "/currency-conversion-feign/${segment}"))
                .uri("lb://currency-conversion");

        return builder.routes()
                .route(routeFunction)
                // .path().uri() asks the discovery service for currency-exchange service and then redirects to that service
                .route(p -> p.path("/currency-exchange/**").uri("lb://currency-exchange"))
                .route(p -> p.path("/currency-conversion/**").uri("lb://currency-conversion"))
                .route(p -> p.path("/currency-conversion-feign/**").uri("lb://currency-conversion"))
                .route(fullRewrite)
                .build();
    }
}
