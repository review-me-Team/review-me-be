package reviewme.be.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
            .title("ReviewMe API Docs")
            .description("ReviewMe API Docs")
            .description(
                "<h3> Review-me API</h3><img src=\"https://avatars.githubusercontent.com/u/149453150?s=200&v=4\" width=150 height=150 />")
            .contact(new Contact()
                .name("ReviewMe")
                .email("gseonggyu968@gmail.com")
                .url("https://github.com/review-me-Team"))
            .version("v0.0.1");

        return new OpenAPI()
            .components(new Components())
            .info(info)
            .addServersItem(new Server().url("https://api.review-me.co.kr"));
    }

}
