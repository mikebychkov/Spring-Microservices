package org.mike.licenses;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mike.licenses.events.models.OrganizationChangeModel;
import org.mike.licenses.repository.OrganizationRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import java.util.Collections;
import java.util.List;

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

    public static void main(String[] args) {
        SpringApplication.run(LicensingServiceApplication.class, args);
    }
}
