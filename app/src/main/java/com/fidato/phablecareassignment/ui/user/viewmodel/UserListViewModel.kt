package com.fidato.phablecareassignment.ui.user.viewmodel

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fidato.phablecareassignment.base.BaseViewModel
import com.fidato.phablecareassignment.constants.Constants
import com.fidato.phablecareassignment.constants.Constants.Companion.USER_ADDED
import com.fidato.phablecareassignment.constants.Constants.Companion.USER_DELETED
import com.fidato.phablecareassignment.constants.Constants.Companion.USER_UPDATED
import com.fidato.phablecareassignment.data.model.UserModel
import com.fidato.phablecareassignment.db.DatabaseHelper
import com.fidato.phablecareassignment.ui.user.adapter.UserAdapter
import com.fidato.phablecareassignment.utility.Resource
import com.fidato.phablecareassignment.utility.isEmailValid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserListViewModel(application: Application, private val dbHelper: DatabaseHelper) :
    BaseViewModel(application) {

    private val TAG: String = this::class.java.canonicalName.toString()

    private val context = application.applicationContext

    lateinit var userAdapter: UserAdapter

    private val users = MutableLiveData<Resource<List<UserModel>>>()

    var errUserName = MutableLiveData<String>("")
    var errUserEmail = MutableLiveData<String>("")

    var user: UserModel = UserModel()

    var isEdit: Boolean = false

    var saveUserResponse = MutableLiveData<Resource<String>>()

    var deleteUserResponse = MutableLiveData<Resource<String>>()

    fun getUsersList() = users

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        viewModelScope.launch {
            users.postValue(Resource.loading(null))
            try {
                val usersFromDb = dbHelper.getUsers()
                users.postValue(Resource.success(usersFromDb))
            } catch (e: Exception) {
                e.printStackTrace()
                users.postValue(Resource.error(null, "Something Went Wrong"))
            }
        }
    }

    fun validateUserData() {
        if (!user.name.trim().isEmpty()) {
            if (!user.email.trim().isEmpty()) {
                if (user.email.isEmailValid()) {
                    user.name = user.name.trim()
                    user.email = user.email.trim()
                    saveUser()
                } else {
                    errUserEmail.value = Constants.ERR_VALID_EMAIL
                }
            } else {
                errUserEmail.value = Constants.ERR_USER_EMAIL
            }
        } else {
            errUserName.value = Constants.ERR_USER_NAME
        }
    }

    fun saveUser() = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            saveUserResponse.value = Resource.loading(null)
        }
        try {
            if (!isEdit) {
                val userList = ArrayList<UserModel>()
                userList.add(user)
                dbHelper.insertAll(userList)
            } else {
                dbHelper.udpateUser(user)
            }

            withContext(Dispatchers.Main) {
                if (isEdit) {
                    saveUserResponse.value = Resource.success(USER_UPDATED)
                } else {
                    saveUserResponse.value = Resource.success(USER_ADDED)
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                saveUserResponse.value = Resource.error(null, e.localizedMessage)
            }
        }
    }

    fun deleteUser(deleteUser: UserModel) = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            deleteUserResponse.value = Resource.loading(null)
        }
        try {
            dbHelper.deleteUser(deleteUser)
            withContext(Dispatchers.Main) {
                deleteUserResponse.value = Resource.success(USER_DELETED)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                deleteUserResponse.value = Resource.error(null, e.localizedMessage)
            }
        }
    }

    fun setDataFromBundle(catBundle: Bundle?) {
        if (catBundle != null) {
            isEdit = catBundle.getBoolean("BUNDLE_EDIT_USER")
            if (isEdit)
                user = catBundle.getParcelable<UserModel>("BUNDLE_USER")!!
        }
    }

}