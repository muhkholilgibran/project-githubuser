package com.example.githubuser.data.ui.main

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.adapter.ListUserAdapter
import com.example.githubuser.data.helper.SettingPreferences
import com.example.githubuser.data.helper.ViewModelFactory
import com.example.githubuser.data.response.User
import com.example.githubuser.data.ui.mode.ModeActivity
import com.example.githubuser.data.ui.mode.ThemeViewModel
import com.example.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SETTING)
    private val viewModel by viewModels<AllViewModels>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var listAdapter: ListUserAdapter
    private lateinit var themeViewModel: ThemeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val toolbar = findViewById<Toolbar>(R.id.Toolbar1)

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

        val applicationInstance: Application = application
        val pref = SettingPreferences.getInstance(applicationInstance.dataStore)
        val factory = ViewModelFactory.getInstance(applicationInstance, pref)
        themeViewModel = ViewModelProvider(this, factory).get(ThemeViewModel::class.java)

        setupUI()
        observeUserData()
        observeThemeSettings()

    }


    private fun observeThemeSettings() {
        themeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                R.drawable.ic_favorite_dark
                R.drawable.setting_dark

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                R.drawable.ic_favorite_light
                R.drawable.setting_light
            }
        }
    }



    private fun setupUI() {
        listAdapter = ListUserAdapter()
        listAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                navigateToUserDetail(user)
            }
        })

        binding.apply {
            ResultSearch.layoutManager = LinearLayoutManager(this@MainActivity)
            ResultSearch.setHasFixedSize(true)
            ResultSearch.adapter = listAdapter

            btnSearch.setOnClickListener {
                val query = Search.text.toString()
                if (query.isNotEmpty()) {
                    showLoading(true)
                    viewModel.searchUsers(query)
                }
            }

            Search.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    val query = Search.text.toString()
                    if (query.isNotEmpty()) {
                        showLoading(true)
                        viewModel.searchUsers(query)
                    }
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }
    }


    private fun observeUserData() {
        viewModel.users().observe(this) { users ->
            listAdapter.updateData(users)
            showLoading(false)
        }

    }

    private fun navigateToUserDetail(user: User) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_USERNAME, user.login)
            putExtra(DetailActivity.EXTRA_NAME, user.name)
            putExtra(DetailActivity.EXTRA_ID, user.id)
            putExtra("avatar", user.avatarUrl)
        }
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.ProgressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    companion object {
        const val ARG_LOGIN = "login"
        const val SETTING = "settings"
    }

}
