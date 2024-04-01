package com.example.githubuser.data.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.database.FavUser
import com.example.githubuser.data.repository.FavUserRepository

class FavoriteViewModel(application: Application) :
    ViewModel() {

    private val mFavRepository: FavUserRepository = FavUserRepository(application)

    fun getAllFav(): LiveData<List<FavUser>> = mFavRepository.getAllDatabase()
    fun getFav(username: String): LiveData<List<FavUser>> = mFavRepository.getFavUserByUsername(username)

}