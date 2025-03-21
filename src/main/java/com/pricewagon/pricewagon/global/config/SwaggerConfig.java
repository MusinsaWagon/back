package com.pricewagon.pricewagon.global.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.ServletContext;

@Configuration
public class SwaggerConfig {
	private static final String PROJECT_NAME = "MusinsaWagon";
	private static final String SERVER_NAME = PROJECT_NAME + " Server";
	private static final String API_TITLE = PROJECT_NAME + " 서버 API 문서";
	private static final String API_DESCRIPTION = PROJECT_NAME + " 서버 API 문서입니다";
	private static final String GITHUB_URL = "https://github.com/MusinsaWagon/back";

	@Value("${swagger.version}")
	private String version;

	@Bean
	public OpenAPI openAPI(ServletContext servletContext) {
		Server server = new Server().url(servletContext.getContextPath()).description(API_DESCRIPTION);
		String jwtSchemeName = "JWT TOKEN";
		// API 요청헤더에 인증정보 포함
		SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
		// SecuritySchemes 등록
		Components components = new Components()
			.addSecuritySchemes(jwtSchemeName, new SecurityScheme()
				.name(jwtSchemeName)
				.type(SecurityScheme.Type.HTTP) // HTTP 방식
				.scheme("bearer")
				.bearerFormat("JWT"));

		return new OpenAPI()
			.servers(List.of(server))
			.addSecurityItem(securityRequirement)
			.components(components)
			.info(swaggerInfo());
	}

	private Info swaggerInfo() {
		License license = new License();
		license.setUrl(GITHUB_URL);
		license.setName(SERVER_NAME);

		return new Info()
			.version("v" + version)
			.title(API_TITLE)
			.description(API_DESCRIPTION)
			.license(license);
	}

}
