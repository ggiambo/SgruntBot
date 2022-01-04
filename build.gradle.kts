group = "fdtheroes"
version = "1.0-SNAPSHOT"
description = "SgruntBot"
java.sourceCompatibility = JavaVersion.VERSION_11

plugins {
    kotlin("jvm") version "1.6.10"
    application
    id("com.github.ben-manes.versions") version "0.40.0"
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", "1.6.10")
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.6.0")
    implementation("org.telegram", "telegrambots", "5.6.0")
    implementation("org.reflections", "reflections", "0.10.2")
    implementation("ch.qos.logback", "logback-classic", "1.2.10")
    implementation("org.mariadb.jdbc", "mariadb-java-client", "2.7.4")
    implementation("org.jetbrains.exposed", "exposed-core", "0.37.3")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.37.3")
    implementation("org.jetbrains.exposed", "exposed-java-time", "0.37.3")
    testImplementation("org.junit.platform", "junit-platform-launcher", "1.8.2")
    testImplementation(platform("org.junit:junit-bom:5.8.2"))
    testImplementation("org.junit.jupiter", "junit-jupiter")
    testImplementation("org.assertj", "assertj-core", "3.22.0")
    testImplementation("org.mockito.kotlin", "mockito-kotlin", "4.0.0")
}

application {
    mainClass.set("com.fdtheroes.sgruntbot.MainKt")
}

tasks.test {
    useJUnitPlatform()
    maxParallelForks = Runtime.getRuntime().availableProcessors()
    testLogging {
        events("passed", "skipped", "failed")
    }
}


