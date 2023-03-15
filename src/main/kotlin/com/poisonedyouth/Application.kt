package com.poisonedyouth

import com.poisonedyouth.application.setupApplicationConfiguration
import com.poisonedyouth.persistence.migrateDatabaseSchema
import com.poisonedyouth.persistence.setupDatabase
import com.poisonedyouth.plugins.configureRouting
import com.poisonedyouth.plugins.configureSerialization
import installKoin
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>): Unit =
    EngineMain.main(args)

fun Application.module() {
    setupApplicationConfiguration()
    val dataSource = setupDatabase()
    migrateDatabaseSchema(dataSource)
    installKoin()
    configureSerialization()
    configureRouting()
}
