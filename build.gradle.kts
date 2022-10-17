import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "fdtheroes"
version = "1.0-SNAPSHOT"
description = "SgruntBot"

plugins {
    application
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.spring") version "1.7.10"
    id("org.springframework.boot") version "3.0.0-M5"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
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
    implementation("org.telegram", "telegrambots", "6.1.0")
    implementation("org.jsoup", "jsoup", "1.15.3")
    implementation("org.springdoc", "springdoc-openapi-ui", "1.6.11")
    implementation("org.springdoc", "springdoc-openapi-kotlin", "1.6.11")
    implementation("org.springdoc", "springdoc-openapi-webmvc-core", "1.6.11")
    implementation("org.knowm.xchart", "xchart", "3.8.2")
    runtimeOnly("org.mariadb.jdbc", "mariadb-java-client")
    runtimeOnly("com.h2database", "h2")
    annotationProcessor("org.springframework.boot", "spring-boot-configuration-processor")
    testImplementation("org.springframework.boot", "spring-boot-starter-test")
    testImplementation("org.mockito.kotlin", "mockito-kotlin", "4.0.0")
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
