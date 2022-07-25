package com.hackbyte.di

import com.hackbyte.data.user.MongoUserDataSource
import com.hackbyte.data.user.UserDataSource
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val user = module {
    singleOf(::MongoUserDataSource) {
        bind<UserDataSource>()
    }
}