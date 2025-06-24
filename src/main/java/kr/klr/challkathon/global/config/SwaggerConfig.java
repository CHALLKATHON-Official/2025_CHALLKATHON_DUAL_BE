package kr.klr.challkathon.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
		tags = {
				@Tag(name = "admin", description = "기타 관리자용"),
				@Tag(name = "repository", description = "repository 관련 API"),
				@Tag(name = "memo", description = "메모 관련 API"),
				@Tag(name = "commit", description = "commit 관련 API"),
				@Tag(name = "pull-request", description = "pull-request 관련 API"),
				@Tag(name = "file", description = "파일 관련 API"),
				@Tag(name = "collaboration", description = "협업 관련 API"),
				@Tag(name = "auth", description = "인증 관련 API"),
				@Tag(name = "version", description = "버전 정보 관련 API"),
				@Tag(name = "recruiting", description = "모집 공고 관련 API"),
				@Tag(name = "portfolio", description = "포트폴리오 관련 API"),
				@Tag(name = "portfolio-project", description = "포트폴리오 내의 프로젝트들 관련 API"),
				@Tag(name = "aws", description = "aws 관련 API"),
		}
)

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		Info info = new Info()
			.title("Notinoty API")
			.version("1.0")
			.description("Notinoty 서비스를 위한 API 명세입니다.");

		return new OpenAPI()
			.info(info)
			.addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
			.components(new io.swagger.v3.oas.models.Components()
				.addSecuritySchemes("Bearer Authentication", new SecurityScheme()
					.type(SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.bearerFormat("JWT")));
	}
}
