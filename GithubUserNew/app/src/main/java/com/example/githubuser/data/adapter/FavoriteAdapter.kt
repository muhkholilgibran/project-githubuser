package com.example.githubuser.data.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.database.FavUser
import com.example.githubuser.data.response.User
import com.example.githubuser.databinding.ItemRowUserBinding

class FavoriteAdapter : ListAdapter<FavUser, FavoriteAdapter.ViewHolder>(CALLBACK)  {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemCLickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(private val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: FavUser) {

            binding.Username.text = item.username
            binding.apply {
                Glide.with(itemView.context)
                    .load(item.avatarUrl)
                    .centerCrop()
                    .error(R.drawable.ic_profile)
                    .into(binding.listUserImg)
                Username.text = item.username
            }
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(item, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        } else {

        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: FavUser, position: Int)
    }

    companion object {
        private val CALLBACK = object : DiffUtil.ItemCallback<FavUser>() {
            override fun areItemsTheSame(oldItem: FavUser, newItem: FavUser): Boolean {
                return oldItem.username == newItem.username
            }

            override fun areContentsTheSame(oldItem: FavUser, newItem: FavUser): Boolean {
                return oldItem == newItem
            }
        }
    }
}