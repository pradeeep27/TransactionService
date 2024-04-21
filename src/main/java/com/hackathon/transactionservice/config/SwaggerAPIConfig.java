package com.hackathon.transactionservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerAPIConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().info(new Info().title("Transaction Service")
                .description("Transaction Service - Swagger UI")
                .version("1.0.0"));
    }


}
