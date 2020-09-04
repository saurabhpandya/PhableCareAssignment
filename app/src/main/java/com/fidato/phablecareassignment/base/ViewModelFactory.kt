package com.fidato.phablecareassignment.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fidato.phablecareassignment.db.DatabaseHelper
import com.fidato.phablecareassignment.ui.user.viewmodel.UserListViewModel

class ViewModelFactory<T>(private val dbHelper: T, private val application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(UserListViewModel::class.java)) {
            return UserListViewModel(application, dbHelper as DatabaseHelper) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }
}