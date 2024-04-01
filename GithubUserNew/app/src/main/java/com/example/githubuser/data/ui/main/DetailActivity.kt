package com.example.githubuser.data.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewStub
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.view.menu.MenuView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.adapter.SectionsPagerAdapter
import com.example.githubuser.data.database.FavUser
import com.example.githubuser.data.helper.ViewModelFactory
import com.example.githubuser.data.response.User
import com.example.githubuser.data.ui.insert.FavAddViewModel
import com.example.githubuser.data.ui.mode.ModeActivity
import com.example.githubuser.data.ui.mode.ThemeViewModel
import com.example.githubuser.databinding.ActivityDetailBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch


class DetailActivity : AppCompatActivity(){
    private lateinit var username: String
    private lateinit var avatarUrl: String
    private lateinit var themeViewModel: ThemeViewModel
    private lateinit var favAddViewModel: FavAddViewModel
    private var favList: List<FavUser>? = null

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_IMAGE = "extra_avatarUrl"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: AllViewModels

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.Toolbar2)

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu2 -> {
                    val intent = Intent(this, FavoriteActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu3 -> {
                    val intent = Intent(this, ModeActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        avatarUrl = intent.getStringExtra("avatar") ?:""
        favAddViewModel = obtainViewModel(this@DetailActivity)

        val username = intent.getStringExtra(EXTRA_USERNAME)

        if (username.isNullOrEmpty()) {
            finish()
            return
        }
        val bundle = Bundle().apply {
            putString(EXTRA_USERNAME, username)
        }

        val fabButton: FloatingActionButton = findViewById(R.id.favAdd)
        val modelFav = obtainViewModelFav(this@DetailActivity)


       modelFav.getFav(username).observe(this@DetailActivity) { list ->
            favList = list
        }

        fabButton.setOnClickListener {
            val fav = FavUser()
            fav.username = username
            fav.avatarUrl = avatarUrl

            if (favList.isNullOrEmpty() || favList!![0].username != username) {
                favAddViewModel.insert(fav)
                showToast(getString(R.string.add))
            } else {
                favAddViewModel.delete(fav)
                showToast(getString(R.string.remove))
            }
        }

        setupViewModel()
        observeDetailUser()
        setupViewPager(bundle)
    }


    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
    private fun setupViewModel() {
        detailViewModel = ViewModelProvider(this)[AllViewModels::class.java]
        lifecycleScope.launch {
            detailViewModel.setDetailUsers(intent.getStringExtra(EXTRA_USERNAME)!!)

        }
    }

    private fun observeDetailUser() {
        detailViewModel.detailUser().observe(this) { detailUser ->
            if (detailUser != null) {
                with(binding) {
                    Name.text = detailUser.name
                    Username.text = detailUser.login
                    detailFollowers.text = "${detailUser.followers} Pengikut"
                    detailFollowing.text = "${detailUser.following} Mengikuti"
                    Glide.with(this@DetailActivity)
                        .load(detailUser.avatarUrl)
                        .centerCrop()
                        .into(ImgProfile)
                    showLoading(false)
                }
            }

        }

    }

    private fun setupViewPager(bundle: Bundle) {
        val sectionPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, bundle)
        binding.viewPager.adapter = sectionPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)

        for (i in TAB_TITLES.indices) {
            binding.tabs.getTabAt(i)?.setText(TAB_TITLES[i])
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.LoadingDetail.visibility = if (isLoading) {
            View.VISIBLE
            Log.d("DetailActivity", "showLoading: $isLoading")
        } else {
            View.GONE
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavAddViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity,factory)[FavAddViewModel::class.java]
    }
    private fun obtainViewModelFav(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }


}



