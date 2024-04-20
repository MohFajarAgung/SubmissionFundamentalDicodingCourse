package com.dicoding.sub1fundamental.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(gituhubUser: GithubUser)

    @Query("SELECT * from githubuser")
    fun getAllUsers(): LiveData<List<GithubUser>>

    @Query("SELECT COUNT(*) FROM githubuser WHERE id = :userId")
    fun getUserCount(userId:Int): Int


    @Query("DELETE FROM githubuser where id = :userId")
    fun delete(userId: Int):Int

}