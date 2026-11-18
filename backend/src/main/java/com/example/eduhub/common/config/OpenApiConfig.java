package com.example.eduhub.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 문서의 제목·버전 (NFR-04). 문서: /v3/api-docs, 화면: /swagger-ui.html
 *
 * <p>프론트 클라이언트는 이 스펙에서 생성한다.
 */
@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI eduHubOpenApi() {
    return new OpenAPI()
        .info(new Info().title("EduHub API").description("사내 교육과정 조회·수강 신청 시스템").version("v1"));
  }
}
