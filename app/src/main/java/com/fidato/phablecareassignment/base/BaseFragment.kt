package com.fidato.phablecareassignment.base

import androidx.fragment.app.Fragment
import com.fidato.phablecareassignment.MainActivity

open class BaseFragment : Fragment() {
    private val TAG: String = this::class.java.canonicalName.toString()

    fun updateTitle(title: String?) {
        (activity as MainActivity).supportActionBar?.title = title ?: activity?.actionBar?.title
    }

    fun onBackPressed(){
        activity?.onBackPressed()
    }

}