package com.example.githubuser.data.retrofit
import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.data.response.MainResponse
import com.example.githubuser.data.response.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    fun searchUsers(
        @Query("q") q: String
    ): Call<MainResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>

}