package com.example.githubuser.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubuser.data.response.User

@Database(entities = [FavUser::class], version = 1)
abstract class GitUserRoomDatabase : RoomDatabase() {
    abstract fun favUserDao(): FavUserDao
    companion object {
        @Volatile
        private var INSTANCE: GitUserRoomDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): GitUserRoomDatabase {
            if (INSTANCE == null) {
                synchronized(GitUserRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        GitUserRoomDatabase::class.java, "GitUser_database")
                        .build()
                }
            }
            return INSTANCE as GitUserRoomDatabase
        }
    }
}