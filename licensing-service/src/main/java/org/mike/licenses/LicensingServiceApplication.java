package org.mike.licenses;

import brave.sampler.Sampler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

//@EnableEurekaClient     // OPTIONAL, DEPENDENCY USAGE IS ENOUGH (netflix-eureka-client)
//@EnableDiscoveryClient  // USED WHEN IMPLEMENTING SPRING DISCOVERY SERVICE
//@EnableFeignClients     // USED WHEN IMPLEMENTING FEIGN LIBRARIES
@RefreshScope
@SpringBootApplication
@EnableCircuitBreaker       // HYSTRIX LIBRARY
@EnableResourceServer       // ENABLING OAUTH2 SERVER PROTECTION
public class LicensingServiceApplication {

    private static final Logger log = LogManager.getLogger(LicensingServiceApplication.class);

//    @LoadBalanced
//    @Bean
//    public RestTemplate getRestTemplate(){
//        RestTemplate template = new RestTemplate();
////        List interceptors = template.getInterceptors();
////        if (interceptors == null) {
////            template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
////        } else {
////            interceptors.add(new UserContextInterceptor());
////            template.setInterceptors(interceptors);
////        }
//        return template;
//    }

    @LoadBalanced
    @Bean
    public OAuth2RestTemplate oauth2RestTemplate(@Qualifier("oauth2ClientContext")  OAuth2ClientContext oauth2ClientContext,
                                                 OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, oauth2ClientContext);
    }

    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }

    public static void main(String[] args) {
        SpringApplication.run(LicensingServiceApplication.class, args);
    }
}
