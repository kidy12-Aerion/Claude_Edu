plugins {
    // 로컬에 JDK 21이 없으면 Gradle이 툴체인용 JDK를 내려받는다 (~/.gradle/jdks)
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "eduhub-backend"
