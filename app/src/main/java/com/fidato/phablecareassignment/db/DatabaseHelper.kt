package com.fidato.phablecareassignment.db

import com.fidato.phablecareassignment.data.model.UserModel

interface DatabaseHelper {
    suspend fun getUsers(): List<UserModel>

    suspend fun insertAll(users: List<UserModel>)

    suspend fun udpateUser(updateUser: UserModel)

    suspend fun deleteUser(deleteUser: UserModel)

}