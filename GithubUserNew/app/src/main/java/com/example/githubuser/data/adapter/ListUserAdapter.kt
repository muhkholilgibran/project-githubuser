package com.example.githubuser.data.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.data.response.User
import com.example.githubuser.databinding.ItemRowUserBinding


class ListUserAdapter :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private val listUser = ArrayList<User>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: User) {
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(user)
            }
            binding.apply {
                Glide.with(itemView)
                    .load(user.avatarUrl)
                    .centerCrop()
                    .into(listUserImg)
                Username.text = user.login

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser [position])
    }

    override fun getItemCount(): Int = listUser.size

    fun updateData(newList: ArrayList<User>) {
        Log.d("ListUserAdapter", "updateData called")
        listUser.clear()
        listUser.addAll(newList)
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: User)
    }
}