package com.fidato.phablecareassignment.ui.user.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fidato.phablecareassignment.R
import com.fidato.phablecareassignment.base.BaseFragment
import com.fidato.phablecareassignment.base.ViewModelFactory
import com.fidato.phablecareassignment.databinding.FragmentAddEditUserBinding
import com.fidato.phablecareassignment.db.DatabaseBuilder
import com.fidato.phablecareassignment.db.DatabaseHelperImpl
import com.fidato.phablecareassignment.ui.user.viewmodel.UserListViewModel
import com.fidato.phablecareassignment.utility.Status
import com.fidato.phablecareassignment.utility.showToast

class AddEditUserFragment : BaseFragment() {
    private val TAG: String = this::class.java.canonicalName.toString()

    private lateinit var binding: FragmentAddEditUserBinding

    private lateinit var viewModel: UserListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditUserBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setListner()
        observeData()
        getData()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this.requireActivity(),
            ViewModelFactory(
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity())),
                this.requireActivity().application
            )
        ).get(UserListViewModel::class.java)
        binding.vm = viewModel
    }

    private fun setListner() {
        binding.tiedttxtUserName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.errUserName.postValue("")
            }
        })
        binding.tiedttxtUserEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.errUserEmail.postValue("")
            }
        })
    }

    private fun getData() {
        viewModel.setDataFromBundle(arguments)
        if (viewModel.isEdit) {
            updateTitle(requireActivity().getString(R.string.title_edit_user))
        } else {
            updateTitle(requireActivity().getString(R.string.title_add_user))
        }
        binding.user = viewModel.user
        binding.executePendingBindings()
    }

    private fun observeData() {
        viewModel.saveUserResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.prgrs.visibility = View.GONE
                    requireActivity().showToast(it.data!!)
                    onBackPressed()
                }
                Status.LOADING -> {
                    binding.prgrs.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    //Handle Error
                    binding.prgrs.visibility = View.GONE
                    requireActivity().showToast(it.message!!)

                }
            }
        })
    }

}