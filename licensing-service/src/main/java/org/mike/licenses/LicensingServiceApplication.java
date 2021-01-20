package org.mike.licenses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient     // OPTIONAL, DEPENDENCY USAGE IS ENOUGH (netflix-eureka-client)
@EnableDiscoveryClient  // USED WHEN IMPLEMENTING SPRING DISCOVERY SERVICE
@EnableFeignClients     // USED WHEN IMPLEMENTING FEIGN LIBRARIES
@RefreshScope
@EnableCircuitBreaker   // HYSTRIX LIBRARY
public class LicensingServiceApplication {

    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(LicensingServiceApplication.class, args);
    }
}
