plugins {
    kotlin("jvm") version "2.3.20"
    id("application")
}

group = "com.library"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("io.mockk:mockk:1.13.10")
    implementation("org.yaml:snakeyaml:2.6")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("com.library.MainKt")
    kotlin {
        jvmToolchain(21)
    }
}
