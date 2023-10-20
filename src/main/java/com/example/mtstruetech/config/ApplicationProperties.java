package com.example.mtstruetech.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    private  String baseUrl;
    private  String token;
    private  int cpuLoadMax;
    private  int memoryLoadMax;
    private  int delta;
}