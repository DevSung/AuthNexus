package com.example.authnexus.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    // 주소: http://localhost:8003/swagger-ui/index.html
    @Bean
    protected OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("AuthNexus Api").version("0.0.1"))
                .components(new Components()
                        .addSecuritySchemes("Token", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")))
                .addSecurityItem(new SecurityRequirement().addList("Token"));
    }

}
