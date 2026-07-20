plugins {
    kotlin("jvm") version "2.3.20"
    id("application")
}

application {
    mainClass.set("com.library.MainKt")
}

repositories {
    mavenCentral()
}
