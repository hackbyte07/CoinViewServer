package com.hackbyte.di

import com.hackbyte.security.hashing.HashingService
import com.hackbyte.security.hashing.SHA256HashingService
import com.hackbyte.security.token.JwtTokenService
import com.hackbyte.security.token.TokenService
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val security = module {
    singleOf(::JwtTokenService) {
        bind<TokenService>()
    }
    singleOf(::SHA256HashingService) {
        bind<HashingService>()
    }
}