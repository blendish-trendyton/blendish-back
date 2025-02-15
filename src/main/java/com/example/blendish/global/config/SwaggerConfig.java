package com.example.blendish.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        final String securitySchemeName = "bearerAuth";

        Server productionServer = new Server()
                .url("https://junyeongan.store")
                .description("Blendish Production Server");

        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Blendish Local Server");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .info(apiInfo())
                .servers(List.of(productionServer, localServer));
    }

    private Info apiInfo() {
        return new Info()
                .title("Blendish")
                .description("나만의 레시피 만들기")
                .version("1.0.0"); // API의 버전
    }
}
