package com.poisonedyouth.application

import io.ktor.server.application.Application

object ApplicationConfiguration {
    lateinit var databaseConfig: DatabaseConfig
}

fun Application.setupApplicationConfiguration() {
    // Database
    val databaseObject = environment.config.config("ktor.database")
    val driverClass = databaseObject.property("driverClass").getString()
    val url = databaseObject.property("url").getString()
    val user = databaseObject.property("user").getString()
    val password = databaseObject.property("password").getString()
    val maxPoolSize = databaseObject.property("maxPoolSize").getString().toInt()
    ApplicationConfiguration.databaseConfig = DatabaseConfig(
        driverClass = driverClass,
        url = url,
        user = user,
        password = password,
        maxPoolSize = maxPoolSize
    )
}

data class DatabaseConfig(
    val driverClass: String,
    val url: String,
    val user: String,
    val password: String,
    val maxPoolSize: Int
)
