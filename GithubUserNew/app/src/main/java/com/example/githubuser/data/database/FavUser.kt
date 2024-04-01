package com.example.githubuser.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "FavoriteData")
@Parcelize
data class FavUser (
    @PrimaryKey(autoGenerate = false)

    @ColumnInfo("username")
    var username: String = "",

    @ColumnInfo("avatarUrl")
    var avatarUrl: String? = null

) : Parcelable