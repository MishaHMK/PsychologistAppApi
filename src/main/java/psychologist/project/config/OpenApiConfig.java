package psychologist.project.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    private static final String KEY = "BearerAuth";
    private static final String SCHEME = "bearer";
    private static final String FORMAT = "JWT";

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mindbloom API")
                        .version("v1")
                        .description("API documentation for the Mindbloom backend"))
                .addServersItem(new Server().url("https://www.mindbloom-api.store/api"))
                .addServersItem(new Server().url("http://localhost:8080/api"))
                .addServersItem(new Server().url("http://localhost:8088/api"))
                .components(new Components().addSecuritySchemes(KEY,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme(SCHEME)
                                .bearerFormat(FORMAT)))
                .addSecurityItem(new SecurityRequirement().addList(KEY));
    }
}
