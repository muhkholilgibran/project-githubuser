package com.example.githubuser.data.ui.mode

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.R
import com.example.githubuser.data.helper.SettingPreferences
import com.example.githubuser.data.helper.ViewModelFactory
import com.example.githubuser.databinding.ActivityModeBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("mode")

class ModeActivity : AppCompatActivity() {

        private lateinit var binding: ActivityModeBinding


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityModeBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val switchTheme = binding.switchMode
            val applicationInstance: Application = application
            val pref = SettingPreferences.getInstance(dataStore)
            val viewModel = ViewModelProvider(this, ViewModelFactory(applicationInstance, pref))[ThemeViewModel::class.java]
            viewModel.getThemeSettings().observe(
                this
            ) { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                }
            }

            switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                viewModel.saveThemeSetting(isChecked)
            }
        }
}
