import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring") version "2.0.0"
    kotlin("plugin.jpa") version "2.0.0"
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"
    id("com.github.ben-manes.versions") version "0.51.0"
    id("com.glovoapp.semantic-versioning") version "1.1.10"
}

configurations.all {
    exclude("commons-logging", "commons-logging")
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
    implementation("org.springframework.boot", "spring-boot-starter-data-jpa")
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("org.springframework.boot", "spring-boot-starter-cache")
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin")
    implementation("org.jetbrains.kotlin", "kotlin-reflect")
    implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core")
    implementation("com.squareup.okhttp3", "okhttp")
    implementation( "org.telegram", "telegrambots-longpolling", "7.9.1")
    implementation( "org.telegram", "telegrambots-client", "7.9.1")
    implementation("org.jsoup", "jsoup", "1.18.1")
    implementation("org.springdoc", "springdoc-openapi-starter-common", "2.6.0")
    implementation("org.springdoc", "springdoc-openapi-starter-webmvc-ui", "2.6.0")
    implementation("org.knowm.xchart", "xchart", "3.8.8")
    runtimeOnly("org.mariadb.jdbc", "mariadb-java-client")
    runtimeOnly("com.h2database", "h2")
    annotationProcessor("org.springframework.boot", "spring-boot-configuration-processor")
    testImplementation("org.springframework.boot", "spring-boot-starter-test")
    testImplementation("org.junit.jupiter", "junit-jupiter")
    testRuntimeOnly("org.junit.platform", "junit-platform-launcher")
    testImplementation("org.mockito.kotlin", "mockito-kotlin", "5.4.0")
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
