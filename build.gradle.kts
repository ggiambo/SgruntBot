import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val telegrambotsVersion = "7.10.0"
val jsoupVersion = "1.18.1"
val springdocVersion = "2.6.0"
val xchartVersion = "3.8.8"
val mockitokotlinVersion = "5.4.0"

plugins {
    application
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring") version "2.0.0"
    kotlin("plugin.jpa") version "2.0.0"
    id("org.springframework.boot") version "3.3.4"
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
    implementation("org.telegram", "telegrambots-longpolling", telegrambotsVersion)
    implementation("org.telegram", "telegrambots-client", telegrambotsVersion)
    implementation("org.jsoup", "jsoup", jsoupVersion)
    implementation("org.springdoc", "springdoc-openapi-starter-common", springdocVersion)
    implementation("org.springdoc", "springdoc-openapi-starter-webmvc-ui", springdocVersion)
    implementation("org.knowm.xchart", "xchart", xchartVersion)
    implementation("org.eclipse.jgit:org.eclipse.jgit:7.0.0.202409031743-r")

    runtimeOnly("org.mariadb.jdbc", "mariadb-java-client")
    runtimeOnly("com.h2database", "h2")
    testRuntimeOnly("org.junit.platform", "junit-platform-launcher")

    annotationProcessor("org.springframework.boot", "spring-boot-configuration-processor")

    testImplementation("org.springframework.boot", "spring-boot-starter-test")
    testImplementation("org.junit.jupiter", "junit-jupiter")
    testImplementation("org.mockito.kotlin", "mockito-kotlin", mockitokotlinVersion)
}

springBoot {
    buildInfo()
}

kotlin {
    compilerOptions {
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = JvmTarget.JVM_17
    }
    jvmToolchain(17)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
