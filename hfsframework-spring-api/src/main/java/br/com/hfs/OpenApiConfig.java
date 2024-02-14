package br.com.hfs;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;

@Configuration
@OpenAPIDefinition(
	info = @Info(title = "hfsframework-spring-api", version = "1.0", description = "HFS Framework Spring Api (REST E JWT)", 
		contact = @Contact(name = "Henrique Figueiredo de Souza")
	), 
	security = {
		@SecurityRequirement(name = "bearerToken") 
	}
)
@SecuritySchemes({
	@SecurityScheme(name = "bearerToken", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT") 
})
public class OpenApiConfig {
	//http://localhost:8084/swagger-ui/index.html
}