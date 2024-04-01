package com.example.githubuser.data.ui.insert

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.database.FavUser
import com.example.githubuser.data.repository.FavUserRepository

class FavAddViewModel(application: Application) : ViewModel() {
    private val mNoteRepository: FavUserRepository = FavUserRepository(application)
    fun insert(fav: FavUser) {
        mNoteRepository.insert(fav)
    }
    fun delete(fav: FavUser) {
        mNoteRepository.delete(fav)
    }
}