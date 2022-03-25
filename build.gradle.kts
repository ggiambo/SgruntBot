group = "fdtheroes"
version = "1.0-SNAPSHOT"
description = "SgruntBot"
java.sourceCompatibility = JavaVersion.VERSION_11

plugins {
    kotlin("jvm") version "1.6.10"
    application
    id("com.github.ben-manes.versions") version "0.40.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"

    // begin springify
    id("org.springframework.boot") version "2.6.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("plugin.spring") version "1.6.10"
    // end springify
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
    // begin springify
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    runtimeOnly("com.h2database:h2")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // end springify

    implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", "1.6.10")
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.6.0")
    implementation("org.telegram", "telegrambots", "5.7.1")
    implementation("org.reflections", "reflections", "0.10.2")
    implementation("ch.qos.logback", "logback-classic", "1.2.10")
    implementation("org.mariadb.jdbc", "mariadb-java-client", "3.0.3")
    implementation("org.jetbrains.exposed", "exposed-core", "0.37.3")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.37.3")
    implementation("org.jetbrains.exposed", "exposed-java-time", "0.37.3")
    implementation("com.google.code.gson", "gson", "2.9.0")
    implementation("org.jsoup", "jsoup", "1.14.3")
    testImplementation("org.junit.platform", "junit-platform-launcher", "1.8.2")
    testImplementation(platform("org.junit:junit-bom:5.8.2"))
    testImplementation("org.junit.jupiter", "junit-jupiter")
    testImplementation("org.assertj", "assertj-core", "3.22.0")
    testImplementation("org.mockito.kotlin", "mockito-kotlin", "4.0.0")
}

application {
    mainClass.set("com.fdtheroes.sgruntbot.MainKt")
}

tasks{
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "com.example.ApplicationKt"))
        }
    }
}

tasks.test {
    useJUnitPlatform()
    maxParallelForks = Runtime.getRuntime().availableProcessors()
    testLogging {
        events("passed", "skipped", "failed")
    }
}


