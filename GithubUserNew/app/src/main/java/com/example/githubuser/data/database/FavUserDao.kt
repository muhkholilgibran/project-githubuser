package com.example.githubuser.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: FavUser)

    @Query("SELECT * from FavoriteData")
    fun getAllFav(): LiveData<List<FavUser>>

    @Query("SELECT * FROM FavoriteData WHERE username = :username")
    fun getFavUserByUsername(username: String): LiveData<List<FavUser>>

    @Delete
    fun delete(user: FavUser)
}