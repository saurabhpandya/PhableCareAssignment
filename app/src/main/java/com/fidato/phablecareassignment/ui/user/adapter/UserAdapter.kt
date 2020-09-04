package com.fidato.phablecareassignment.ui.user.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fidato.phablecareassignment.R
import com.fidato.phablecareassignment.data.model.UserModel
import com.fidato.phablecareassignment.databinding.RawUserBinding
import com.fidato.phablecareassignment.utility.OnItemClickListner

class UserAdapter(
    private var arylstUsers: ArrayList<UserModel>,
    val onItemClickListner: OnItemClickListner
) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val binding: RawUserBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.raw_user,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arylstUsers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arylstUsers.get(position))
        holder.itemView.setOnClickListener {
            onItemClickListner.onItemClickListner(position)
        }
    }

    fun addUsers(arylstUsers: List<UserModel>) {
        this.arylstUsers.clear()
        this.arylstUsers.addAll(arylstUsers)
        notifyDataSetChanged()
    }

    fun getUser(position: Int): UserModel {
        return arylstUsers[position]
    }

    class ViewHolder(val binding: RawUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(user: UserModel) {
            binding.user = user
            binding.executePendingBindings()
        }
    }

}
