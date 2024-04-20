package com.dicoding.sub1fundamental.repository

import androidx.lifecycle.LiveData
import com.dicoding.sub1fundamental.database.GithubUser
import com.dicoding.sub1fundamental.database.UserDao

class UserRepository(private val userDao: UserDao) {

    val allUsers : LiveData<List<GithubUser>> = userDao.getAllUsers()

    suspend fun insert(user:GithubUser){
        userDao.insert(user)
    }

    suspend fun delete(userId: Int){
        userDao.delete(userId)

    }




}