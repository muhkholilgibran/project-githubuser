package com.example.githubuser.data.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.ui.insert.FavAddViewModel
import com.example.githubuser.data.ui.main.FavoriteViewModel
import com.example.githubuser.data.ui.mode.ThemeViewModel

class ViewModelFactory(private val mApplication: Application, private val pref: SettingPreferences?) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application, pref: SettingPreferences? = null): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application, pref)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(FavAddViewModel::class.java)) {
            return FavAddViewModel(mApplication) as T
        }else if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            return pref?.let { ThemeViewModel(it) } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}