package com.example.recipetreasures.data.repo

import com.example.recipetreasures.data.model.User
import com.example.recipetreasures.data.db.UserDao

class UserRepository(private val userDao: UserDao) {


    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }


    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }


    suspend fun loginUser(email: String, password: String): User? {
        return userDao.loginUser(email, password)
    }


    suspend fun isEmailExists(email: String): Boolean {
        return userDao.isEmailExists(email) > 0
    }
}