@Suppress("DSL_SCOPE_VIOLATION") // Will be fixed with Gradle 8.1 (see https://github.com/gradle/gradle/issues/22797#issuecomment-1422049473)
plugins {
    id("io.gatling.gradle").version("3.9.2")
    kotlin("jvm") version "1.8.10"
}

dependencies {
    implementation("io.gatling:gatling-core:3.9.2")
    implementation("io.gatling:gatling-http-java:3.9.2")
}

gatling {
    simulations = closureOf<PatternFilterable> {
        include("**/*Simulation*.kt")
    }

    systemProperties = mapOf(
        "baseurl" to "http://localhost:8080", // Need to extract to be able to change depend on environment
    )
}

repositories{
    mavenCentral()
}
