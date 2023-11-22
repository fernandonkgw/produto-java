package com.fnaka.spproduto.infrastructure;

import com.fnaka.spproduto.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(WebServerConfig.class, args);
    }
}
