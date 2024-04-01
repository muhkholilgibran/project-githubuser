package com.example.githubuser.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuser.data.database.FavUser
import com.example.githubuser.data.database.FavUserDao
import com.example.githubuser.data.database.GitUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavUserRepository(application: Application) {

    private val mFavUserDao: FavUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = GitUserRoomDatabase.getDatabase(application)
        mFavUserDao = db.favUserDao()
    }
    fun getAllDatabase(): LiveData<List<FavUser>> = mFavUserDao.getAllFav()

    fun insert(favorite: FavUser) {
        executorService.execute { mFavUserDao.insert(favorite) }
    }
    fun delete(favorite: FavUser) {
        executorService.execute {mFavUserDao.delete(favorite) }
    }
    fun getFavUserByUsername(username: String): LiveData<List<FavUser>> = mFavUserDao.getFavUserByUsername(username)
}