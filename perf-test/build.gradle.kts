val gatlingVersion: String by project
val h2Version: String by project

plugins {
    id("io.gatling.gradle").version("3.9.2")
    kotlin("jvm") version "1.8.10"
}

dependencies {
    implementation("io.gatling:gatling-core:$gatlingVersion")
    implementation("io.gatling:gatling-http-java:$gatlingVersion")
    gatling("org.apache.commons:commons-lang3:3.12.0")
    gatlingRuntimeOnly("com.h2database:h2:$h2Version")
}

gatling {
    simulations = closureOf<PatternFilterable> {
        include("**/*Simulation.kt")
    }

    systemProperties = mapOf(
        "baseurl" to "http://localhost:8080", // Need to extract to be able to change depend on environment
    )
}

repositories{
    mavenCentral()
}
