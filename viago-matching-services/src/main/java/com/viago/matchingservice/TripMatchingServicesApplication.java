package com.viago.matchingservice;

import com.netflix.discovery.EurekaNamespace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TripMatchingServicesApplication {
    public static void main(String[] args) {
        SpringApplication.run(TripMatchingServicesApplication.class, args);
    }
}
