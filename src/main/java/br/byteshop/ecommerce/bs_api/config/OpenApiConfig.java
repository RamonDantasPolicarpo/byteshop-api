package br.byteshop.ecommerce.bs_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ByteShop API E-commerce")
                        .version("0.0.1-SNAPSHOT")
                        .description("Documentação RESTful para a gestão de produtos, clientes e pedidos.")
                );
    }
}
