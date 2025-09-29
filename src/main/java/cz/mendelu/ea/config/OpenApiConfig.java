package cz.mendelu.ea.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Music Streaming API")
                        .description("API for managing users, tracks, and favorites in a music streaming application")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Mendel University")
                                .email("support@mendelu.cz")
                                .url("https://www.mendelu.cz"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8090")
                                .description("Local development server")
                ));
    }
} 