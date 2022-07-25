package com.hackbyte

import com.hackbyte.di.db
import com.hackbyte.di.security
import com.hackbyte.di.user
import com.hackbyte.plugins.configureMonitoring
import com.hackbyte.plugins.configureRouting
import com.hackbyte.plugins.configureSecurity
import com.hackbyte.plugins.configureSerialization
import com.hackbyte.security.token.TokenConfig
import io.ktor.server.application.*
import org.koin.core.context.startKoin

fun main(args: Array<String>): Unit =
    io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    val log = environment.log
    log.debug("ktor application started")
    startKoin {
        modules(
            db,
            security,
            user
        )
    }

    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        secret = System.getenv("JWT_SECRET")
    )

    configureMonitoring()
    configureSerialization()
    configureSecurity(tokenConfig)
    configureRouting(tokenConfig)
    log.debug("Application module initialized")
}
