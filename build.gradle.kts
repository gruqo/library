plugins {
    id("org.jetbrains.kotlin.jvm") version "2.4.10"
    id("application")
    id("org.jetbrains.kotlin.plugin.spring") version "2.4.10"
    id("org.jetbrains.kotlin.plugin.jpa") version "2.4.10"
    id("org.springframework.boot") version "3.5.16"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.library"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly("com.h2database:h2")
    implementation("org.yaml:snakeyaml:2.6")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("com.library.LibraryApplicationKt")
    kotlin {
        jvmToolchain(21)
    }
}
