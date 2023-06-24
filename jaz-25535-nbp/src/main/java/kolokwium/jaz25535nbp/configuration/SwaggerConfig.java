package kolokwium.jaz25535nbp.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SwaggerConfig {

    // Sam się uzywa
    @Bean
    OpenAPI openAPI() {
        Info info = new Info()
                .title("Zwracałka średniej walut od daty do daty")
                .description("Opis zwracałki średniej walut.");

        return new OpenAPI().info(info);
    }
}
