package com.hackbyte.data.user

interface UserDataSource {

    suspend fun getUserByUserName(username: String): User?

    suspend fun insertUser(user: User): Boolean

    suspend fun getUserByUserEmail(email: String): User?

}