package com.fidato.phablecareassignment.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fidato.phablecareassignment.data.model.UserModel
import com.fidato.phablecareassignment.data.user.UserDao

@Database(entities = [UserModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}