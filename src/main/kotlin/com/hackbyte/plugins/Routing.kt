package com.hackbyte.plugins

import com.hackbyte.data.user.UserDataSource
import com.hackbyte.routes.authenticate
import com.hackbyte.routes.getSecretInfo
import com.hackbyte.routes.signIn
import com.hackbyte.routes.signup
import com.hackbyte.security.hashing.HashingService
import com.hackbyte.security.token.TokenConfig
import com.hackbyte.security.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting(tokenConfig: TokenConfig) {
    val userDataSource by inject<UserDataSource>()
    val tokenService by inject<TokenService>()
    val hashingSource by inject<HashingService>()
    install(Routing) {
        signIn(userDataSource, tokenService, hashingSource, tokenConfig)
        signup(userDataSource, hashingSource)
        authenticate()
        getSecretInfo()
    }
}
