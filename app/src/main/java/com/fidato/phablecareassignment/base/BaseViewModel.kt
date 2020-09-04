package com.fidato.phablecareassignment.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG: String = this::class.java.canonicalName.toString()
}