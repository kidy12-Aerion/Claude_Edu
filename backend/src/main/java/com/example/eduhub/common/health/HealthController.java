package com.example.eduhub.common.health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 앱이 요청을 받을 수 있는지 확인하는 API. DB 상태는 보지 않는다. */
@Tag(name = "health", description = "서버 상태 확인")
@RestController
@RequestMapping("/api/health")
public class HealthController {

  @Operation(summary = "서버 상태 확인", description = "서버가 떠 있으면 status=UP을 돌려준다")
  @GetMapping
  public HealthResponse health() {
    return new HealthResponse("UP");
  }

  public record HealthResponse(String status) {}
}
