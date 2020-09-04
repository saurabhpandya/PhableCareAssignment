package com.fidato.phablecareassignment.data.user

import androidx.room.*
import com.fidato.phablecareassignment.data.model.UserModel

@Dao
interface UserDao {
    @Query("SELECT * FROM usermodel")
    suspend fun getAll(): List<UserModel>

    @Insert
    suspend fun insertAll(users: List<UserModel>)

    @Delete
    suspend fun delete(user: UserModel)

    @Update
    suspend fun update(user: UserModel)
}