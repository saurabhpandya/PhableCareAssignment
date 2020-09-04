package com.fidato.phablecareassignment.ui.user.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fidato.phablecareassignment.R
import com.fidato.phablecareassignment.base.BaseFragment
import com.fidato.phablecareassignment.base.ViewModelFactory
import com.fidato.phablecareassignment.data.model.UserModel
import com.fidato.phablecareassignment.databinding.FragmentUserListBinding
import com.fidato.phablecareassignment.db.DatabaseBuilder
import com.fidato.phablecareassignment.db.DatabaseHelperImpl
import com.fidato.phablecareassignment.ui.user.adapter.UserAdapter
import com.fidato.phablecareassignment.ui.user.viewmodel.UserListViewModel
import com.fidato.phablecareassignment.utility.OnItemClickListner
import com.fidato.phablecareassignment.utility.Status
import com.fidato.phablecareassignment.utility.showToast

class UserListFragment : BaseFragment(), OnItemClickListner, View.OnClickListener {

    private val TAG: String = this::class.java.canonicalName.toString()

    private lateinit var binding: FragmentUserListBinding

    private lateinit var viewModel: UserListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setListener()
        setupRecyclerView()
        setupObserver()
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

    private fun setListener() {
        binding.fabAddUser.setOnClickListener(this)
    }

    private fun setupRecyclerView() {
        binding.rcyclrvwUserList.layoutManager = LinearLayoutManager(activity)
        binding.rcyclrvwUserList.setHasFixedSize(true)

        viewModel.userAdapter = UserAdapter(ArrayList<UserModel>(), this)
        binding.rcyclrvwUserList.adapter = viewModel.userAdapter
    }

    private fun setupObserver() {
        viewModel.getUsersList().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.prgrs.visibility = View.GONE
                    it.data?.let { users ->
                        if (users.isEmpty()) {
                            binding.noData.visibility = View.VISIBLE
                            binding.rcyclrvwUserList.visibility = View.GONE
                        } else {
                            binding.noData.visibility = View.GONE
                            binding.rcyclrvwUserList.visibility = View.VISIBLE
                            renderList(users)
                        }
                    }
                }
                Status.LOADING -> {
                    binding.prgrs.visibility = View.VISIBLE
                    binding.rcyclrvwUserList.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    binding.prgrs.visibility = View.GONE
                    requireActivity().showToast(it.message!!)

                }
            }
        })

        viewModel.deleteUserResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.prgrs.visibility = View.GONE
                    requireActivity().showToast(it.data!!)
                    viewModel.fetchUsers()
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

    private fun renderList(users: List<UserModel>) {
        viewModel.userAdapter.addUsers(users)
        viewModel.userAdapter.notifyDataSetChanged()
    }

    private fun navigateTo(navigation: UserNavigation, bundle: Bundle?) {
        when (navigation) {
            UserNavigation.ADD_EDIT_USER -> {
                view?.findNavController()
                    ?.navigate(R.id.action_mainFragment_to_addEditUserFragment, bundle)
            }
        }
    }

    private fun navigateToAddUser() {
        val bundle = Bundle()
        bundle.putBoolean("BUNDLE_EDIT_USER", false)
        navigateTo(UserNavigation.ADD_EDIT_USER, bundle)
    }

    private fun navigateToEditUser(position: Int) {
        val bundle = Bundle()
        bundle.putBoolean("BUNDLE_EDIT_USER", true)
        val user = viewModel.userAdapter.getUser(position)
        bundle.putParcelable("BUNDLE_USER", user)
        navigateTo(UserNavigation.ADD_EDIT_USER, bundle)
    }

    private fun showUserOperations(position: Int) {
        val userForOperation = viewModel.userAdapter.getUser(position)
        val options = arrayOf<CharSequence>("Update", "Delete")
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle("Action for ${userForOperation.name}")
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item] == "Update") {
                navigateToEditUser(position)
            } else if (options[item] == "Delete") {
                viewModel.deleteUser(userForOperation)
            }
        })
        builder.show()
    }

    override fun onItemClickListner(position: Int) {
        Log.d(TAG, "onItemClickListner: Position: $position")
        showUserOperations(position)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_add_user -> {
                navigateToAddUser()
            }
        }
    }
}

enum class UserNavigation {
    ADD_EDIT_USER
}
