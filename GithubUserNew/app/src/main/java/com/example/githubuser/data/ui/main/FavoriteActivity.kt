package com.example.githubuser.data.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.githubuser.data.adapter.FavoriteAdapter
import com.example.githubuser.data.database.FavUser
import com.example.githubuser.data.helper.ViewModelFactory
import com.example.githubuser.data.response.User
import com.example.githubuser.data.ui.main.DetailActivity.Companion.EXTRA_USERNAME
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.google.android.material.snackbar.Snackbar

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter
    private val favViewModel by viewModels<FavoriteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = FavoriteAdapter()

        binding.FavAdd.layoutManager = LinearLayoutManager(this)
        binding.FavAdd.adapter = adapter
        binding.FavAdd.setHasFixedSize(true)


        val favViewModel = obtainViewModel(this@FavoriteActivity)
        favViewModel.getAllFav().observe(this) { Fav ->
            if (Fav.isEmpty()) {
                binding.tvFavuser.visibility = View.VISIBLE
                binding.FavAdd.visibility = View.GONE
            } else {
                binding.tvFavuser.visibility = View.GONE
                binding.FavAdd.visibility = View.VISIBLE
                adapter.submitList(Fav)
            }
        }


        adapter.setOnItemCLickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(user: FavUser, position: Int) {
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intent.putExtra(EXTRA_USERNAME, user.username)
                Log.d("Favorite", "Sukses")
                startActivity(intent)
            }
        })

        favViewModel.getAllFav().observe(this) { favUsers ->
            if (favUsers == null) {
                Snackbar.make(binding.root, "Error loading favorites", Snackbar.LENGTH_SHORT).show()
            }
        }
        observeUserData()
    }

    private fun observeUserData() {
        favViewModel.getAllFav().observe(this) { users ->
            val items = arrayListOf<FavUser>()
            users.map {
                val item = FavUser(username = it.username, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            adapter.submitList(items)
        }

    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

}