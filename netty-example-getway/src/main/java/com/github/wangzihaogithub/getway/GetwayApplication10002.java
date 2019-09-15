package com.github.wangzihaogithub.getway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@EnableDiscoveryClient
@SpringBootApplication
public class GetwayApplication10002 {

    public static void main(String[] args) {
        SpringApplication.run(GetwayApplication10002.class, args);
    }

}
