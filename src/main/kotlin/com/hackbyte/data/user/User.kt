package com.hackbyte.data.user

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@kotlinx.serialization.Serializable
data class User(
    val username: String,
    val email: String,
    val password: String,
    val salt: String,
    @BsonId val id: String = ObjectId().toString()
)
