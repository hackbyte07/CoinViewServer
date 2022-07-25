package com.hackbyte.routes

import com.hackbyte.data.auth.AuthRequest
import com.hackbyte.data.auth.AuthResponse
import com.hackbyte.data.user.User
import com.hackbyte.data.user.UserDataSource
import com.hackbyte.security.hashing.HashingService
import com.hackbyte.security.hashing.SaltedHash
import com.hackbyte.security.token.TokenClaim
import com.hackbyte.security.token.TokenConfig
import com.hackbyte.security.token.TokenService
import com.hackbyte.utils.Constants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*




fun Route.signup(
    userDataSource: UserDataSource,
    hashingService: HashingService
) {

    post("/api/v1/signup") {
        val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, "invalid object type")
            return@post
        }

        val checkForData =
            Constants.EMAIL_ADDRESS_PATTERN.matcher(request.email).matches() && request.password.length >= 6
                    && request.username.length >= 6
        if (!checkForData) {
            call.respond(HttpStatusCode.Conflict, "entered data is not valid")
            return@post
        }
        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User(
            username = request.username,
            email = request.email,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )
        val wasAcknowledged = userDataSource.insertUser(user)
        if (!wasAcknowledged) {
            call.respond(HttpStatusCode.Conflict, "user not created")
            return@post
        }
        call.respond(HttpStatusCode.OK, "user created successfully")
    }

}

fun Route.signIn(
    userDataSource: UserDataSource,
    tokenService: TokenService,
    hashingService: HashingService,
    tokenConfig: TokenConfig
) {
    post("/api/v1/signin") {
        val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, "invalid object type")
            return@post
        }
        val user = userDataSource.getUserByUserName(request.username)
        if (user == null) {
            call.respond(HttpStatusCode.Conflict, "invalid user name or password")
            return@post
        }
        val isValidPassword = hashingService.verify(
            request.password, saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )
        if (!isValidPassword) {
            call.respond(HttpStatusCode.Conflict, "invalid user name or password")
            return@post
        }
        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id
            )
        )
        call.respond(HttpStatusCode.OK, message = AuthResponse(token))
    }
}

fun Route.authenticate() {
    authenticate {
        get("authenticate") {
            call.respond(HttpStatusCode.OK, "user is authenticated")
        }
    }
}

fun Route.getSecretInfo() {
    authenticate {
        get("secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(HttpStatusCode.OK, "your user id is $userId")
        }
    }
}