package com.hackbyte.data.user

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoUserDataSource(
    db: CoroutineDatabase
) : UserDataSource {

    private val users = db.getCollection<User>()

    override suspend fun getUserByUserName(username: String): User? =
        users.findOne(User::username eq username)

    override suspend fun insertUser(user: User): Boolean {
        val check =
            users.findOne(User::username eq user.email) == null && users.findOne(User::username eq user.username) == null
        if (!check)
            return false
        return users.insertOne(user).wasAcknowledged()
    }

    override suspend fun getUserByUserEmail(email: String): User? =
        users.findOne(User::email eq email)
}