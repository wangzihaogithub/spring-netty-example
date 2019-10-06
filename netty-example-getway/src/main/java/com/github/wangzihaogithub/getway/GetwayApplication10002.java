package com.github.wangzihaogithub.getway;

import com.github.netty.springboot.EnableNettyEmbedded;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * RequestMappingHandlerAdapter
 * DispatcherHandler
 */
@EnableNettyEmbedded
@EnableWebFlux
@EnableDiscoveryClient
@SpringBootApplication
public class GetwayApplication10002 {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(GetwayApplication10002.class);
        springApplication.setWebApplicationType(WebApplicationType.REACTIVE);
        springApplication.run(args);
    }

}
