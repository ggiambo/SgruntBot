import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.spring") version "1.7.10"
    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.1.0"
    id("com.github.ben-manes.versions") version "0.46.0"
    id("com.glovoapp.semantic-versioning") version "1.1.10"
}


repositories {
    mavenLocal()
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation("org.springframework.boot", "spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("org.springframework.boot", "spring-boot-starter-cache")
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin")
    implementation("org.jetbrains.kotlin", "kotlin-reflect")
    implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
    implementation("org.telegram", "telegrambots", "6.5.0")
    implementation("org.jsoup", "jsoup", "1.15.4")
    implementation("org.springdoc", "springdoc-openapi-ui", "1.6.15")
    implementation("org.springdoc", "springdoc-openapi-kotlin", "1.6.15")
    implementation("org.springdoc", "springdoc-openapi-webmvc-core", "1.6.15")
    implementation("org.knowm.xchart", "xchart", "3.8.3")
    runtimeOnly("org.mariadb.jdbc", "mariadb-java-client")
    runtimeOnly("com.h2database", "h2")
    annotationProcessor("org.springframework.boot", "spring-boot-configuration-processor")
    testImplementation("org.springframework.boot", "spring-boot-starter-test")
    testImplementation("org.mockito.kotlin", "mockito-kotlin", "4.1.0")
}

springBoot {
    buildInfo()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
