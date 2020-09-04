package com.fidato.phablecareassignment.db

import com.fidato.phablecareassignment.data.model.UserModel

class DatabaseHelperImpl(private val appDatabase: AppDatabase) : DatabaseHelper {

    override suspend fun getUsers(): List<UserModel> = appDatabase.userDao().getAll()

    override suspend fun insertAll(users: List<UserModel>) = appDatabase.userDao().insertAll(users)

    override suspend fun udpateUser(updateUser: UserModel) {
        appDatabase.userDao().update(updateUser)
    }

    override suspend fun deleteUser(deleteUser: UserModel) {
        appDatabase.userDao().delete(deleteUser)
    }

}