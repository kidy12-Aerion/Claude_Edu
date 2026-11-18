package com.example.eduhub;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * test 프로필로 앱 전체를 띄운다. Testcontainers PostgreSQL, Flyway, JPA 연결을 함께 확인한다.
 *
 * <p>Docker가 켜져 있어야 한다.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EduHubApplicationTests {

  @Autowired private MockMvc mockMvc;

  @Test
  void contextLoads() {}

  @Test
  void openApiDocsAreServed() throws Exception {
    mockMvc
        .perform(get("/v3/api-docs"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.info.title").value("EduHub API"))
        .andExpect(jsonPath("$.paths['/api/health']").exists());
  }
}
