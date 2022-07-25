package com.hackbyte.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


val db = module {
    singleOf<CoroutineDatabase>() {
        val dbName = "crypto-view"
        val password = System.getenv("MONGODB_PW")
        val client = KMongo.createClient(
            connectionString = "mongodb+srv://hackbyte:$password@cluster0.jtneeos.mongodb.net/$dbName?retryWrites=true&w=majority"
        ).coroutine
        client.getDatabase(dbName)
    }
}