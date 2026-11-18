# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트

EduHub: 사내 교육과정 조회·수강 신청 시스템. 정원이 차면 대기자로 등록되고, 취소가 생기면 대기 1순위가 자동 확정된다.

- 요구사항(FR-01~09, NFR-01~08, 결정 사항 D-xx): `docs/requirements.md`. 작업 전에 해당 ID를 확인한다
- 분석과 미결 질문(Q1~Q7): `docs/requirements-analysis.md`. 답이 정해지지 않은 규칙은 임의로 정하지 말고 질문한다
- 문서, 주석, 커밋 메시지는 한국어로 쓴다

## 현재 상태와 일정

- 1주차(완료): `backend/` 뼈대만 있다. `GET /api/health`, 프로필, OpenAPI, Spotless
- 2주차: `course` 도메인(FR-01~03). 3주차: `enrollment`(FR-04~06). 4~5주차: FR-07~09
- 프론트엔드는 아직 없다. 계획은 React 18 + TypeScript, Module Federation(`shell`, `course-mfe`, `enrollment-mfe`), MUI, Axios, npm workspaces

## 명령어 (backend 폴더에서)

```bash
./gradlew test                      # 테스트. Docker가 켜져 있어야 한다 (Testcontainers)
./gradlew build                     # 테스트 + Spotless 포맷 검사. 커밋 전에 이것으로 확인
./gradlew spotlessApply             # 포맷 자동 정리
./gradlew test --tests "com.example.eduhub.common.health.HealthControllerTest"            # 클래스 하나
./gradlew test --tests "com.example.eduhub.common.health.HealthControllerTest.healthReturnsUp"  # 메서드 하나
./gradlew bootRun --args='--spring.profiles.active=dev'   # 로컬 실행. PostgreSQL과 루트 .env 필요
```

`./gradlew test`는 포맷 검사를 하지 않는다. 포맷까지 보려면 `build`나 `spotlessCheck`를 쓴다.

## 환경 주의

- JDK 21 툴체인을 쓴다. PC에 21이 없으면 foojay 플러그인이 `~/.gradle/jdks`에 받아 온다. Gradle 자체는 JDK 25에서 돌아도 된다
- 회사 네트워크가 HTTPS를 검사한다(Somansa Root CA). Java가 이 인증서를 몰라서 의존성 다운로드가 실패하면 `~/.gradle/gradle.properties`에 `systemProp.javax.net.ssl.trustStoreType=Windows-ROOT`가 있는지 확인한다. curl은 `--ssl-no-revoke`가 필요하다
- 줄바꿈은 LF로 통일한다(`.gitattributes`, Spotless `lineEndings = UNIX`). `*.bat`만 CRLF

## 백엔드 구조

- 패키지는 도메인별로 나눈다(NFR-07): `com.example.eduhub.{course, enrollment, common}`. 각 도메인 안에서 컨트롤러 → 서비스 → 리포지토리 순서로만 호출하고, 엔티티를 API 응답으로 직접 내보내지 않는다. 도메인 패키지의 맡은 일은 `package-info.java`에 적혀 있다
- 스키마는 Flyway(`src/main/resources/db/migration`)만 바꾼다. JPA는 `ddl-auto: validate`라서 엔티티를 추가하면 마이그레이션도 함께 만들어야 앱이 뜬다
- `open-in-view: false`다(NFR-02). 지연 로딩은 서비스 트랜잭션 안에서 끝내고, 목록 조회는 N+1이 없게 DTO 조회나 fetch join을 쓴다
- 프로필
  - `application.yml`: 공통 설정
  - `application-dev.yml`: 접속 정보는 환경 변수(`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`)로만 받는다. Spring Boot는 `.env`를 자동으로 읽지 않아서 `spring.config.import: optional:file:../.env[.properties]`로 루트 `.env`를 읽는다. 새 환경 변수를 추가하면 `.env.example`에 이름만 추가한다
  - `application-test.yml`: `jdbc:tc:postgresql:16-alpine` URL로 Testcontainers가 DB를 띄운다
- 테스트 두 종류
  - `@WebMvcTest`: 웹 계층만, DB 없이
  - `@SpringBootTest` + `@ActiveProfiles("test")`: 실제 PostgreSQL 컨테이너로 전체를 띄운다
  - 동시성·락·제약 조건(NFR-01)은 반드시 실제 PostgreSQL로 검증한다. H2로 바꾸지 않는다
- API 문서: springdoc가 `/api/**`만 문서화한다(`/v3/api-docs`, `/swagger-ui.html`). 프론트 클라이언트는 이 스펙에서 생성할 예정이라(NFR-04), API를 바꾸면 스펙이 맞게 나오는지 확인한다

## 도메인 규칙에서 놓치기 쉬운 것

- 정원·대기 상한·중복 신청은 동시에 100명이 신청해도 지켜져야 한다(NFR-01). 대기 순번 부여와 취소 후 승급도 같은 트랜잭션·락 안에서 처리한다
- D-01: 취소는 회차 시작 1일 전까지. 시간 규칙은 테스트할 수 있게 `Clock`을 주입받아 쓴다(시간대 Asia/Seoul). 정확한 기준 시각은 미결(Q6)
- D-02: 대기는 정원의 50%까지. 소수점 처리는 미결(Q7)
- D-03: 수료 처리는 관리자만

## 저장소 규칙

- 커밋 메시지: `chore:`, `docs:`, `feat:` 같은 접두어 + 한국어 요약, 본문에 바뀐 것을 항목으로 적는다
- 루트의 `00-화면-목록.md`는 다른 프로젝트(성과관리 시스템)의 사내 자료다. 커밋하지 않는다
- 팀원이 AI 없이 구조를 설명할 수 있어야 한다(NFR-08). 공통 베이스 클래스, 제네릭 서비스 같은 과한 추상화를 넣지 않는다
