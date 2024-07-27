package br.com.copyimagem.mscloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableDiscoveryClient
public class MscloudgatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MscloudgatewayApplication.class, args);
	}

	@Bean
	public RouteLocator route( RouteLocatorBuilder builder) {
		return builder
				.routes()
					.route( r -> r.path(
										"/api/v1/customers/**",
										"/api/v1/monthlypayment/**",
										"/api/v1/multi-printer/**")
										.uri("lb://mspersistence"))
					.route( r -> r.path("/api/v1/monthlypayment-service/**")
										.uri("lb://msmonthlypayment"))
				.build();
	}
}
