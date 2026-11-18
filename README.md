# EduHub

사내 교육과정 조회·수강 신청 시스템

## 구성

| 폴더 | 내용 |
|---|---|
| `backend/` | Spring Boot 3, Java 21, Gradle(Kotlin DSL) |
| `docs/` | 요구사항, 분석 문서 |

## 백엔드 실행

필요한 것: JDK 21(없으면 Gradle이 자동으로 내려받음), Docker(테스트용 PostgreSQL)

```bash
cd backend
./gradlew test          # 테스트 (Docker가 켜져 있어야 함)
./gradlew build         # 테스트 + 포맷 검사
./gradlew spotlessApply # 코드 포맷 자동 정리
```

로컬 실행(dev 프로필)은 PostgreSQL이 필요하다. 루트의 `.env.example`을 `.env`로 복사해 접속 정보를 채운 뒤:

```bash
cd backend
./gradlew bootRun --args='--spring.profiles.active=dev'
```

- 상태 확인: http://localhost:8080/api/health
- API 문서: http://localhost:8080/swagger-ui.html
