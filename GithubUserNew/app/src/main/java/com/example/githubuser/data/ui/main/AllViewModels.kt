package com.example.githubuser.data.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.database.FavUser
import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.data.response.MainResponse
import com.example.githubuser.data.response.User
import com.example.githubuser.data.retrofit.ApiConfig
import com.example.githubuser.data.helper.SettingPreferences
import com.example.githubuser.data.repository.FavUserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllViewModels: ViewModel() {

    private val _users = MutableLiveData<ArrayList<User>>()
    private val _detailUser = MutableLiveData<DetailUserResponse>()
    private val _followers = MutableLiveData<ArrayList<User>>()
    private val _following = MutableLiveData<ArrayList<User>>()
    private val _loading = MutableLiveData<Boolean>()

    //DetailUserModel
    fun setDetailUsers(username: String) {
        val config = ApiConfig.instance
            .getUserDetail(username)
            config.enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    if (response.isSuccessful) {
                        _detailUser.postValue(response.body())
                        Log.d("AllViewModels", "Detail user response received: ${response.body()}")
                    } else {
                        Log.e("AllViewModels", "Failed to get detail user, response code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    Log.e("AllViewModels", "Failed to get detail user", t)
                }

            })
    }
    fun detailUser(): LiveData<DetailUserResponse> {
        return _detailUser
    }




    //FollowersModel
    fun getSetFollowers(username: String) {
        val config = ApiConfig.instance
            .getUserFollowers(username)
            config.enqueue(object : Callback<ArrayList<User>>{
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    if (response.isSuccessful){
                        _followers.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }

            })
    }

    fun followers(): LiveData<ArrayList<User>>{
        return _followers
    }


    //FollowingModel
    fun getSetFollowing(username: String) {
        val config = ApiConfig.instance
            .getUserFollowing(username)
            config.enqueue(object : Callback<ArrayList<User>> {
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    if (response.isSuccessful) {
                        _following.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }

            })
    }

    fun following(): LiveData<ArrayList<User>> {
        return _following
    }

    //MainActivityModels

    fun searchUsers(query: String) {
        val config = ApiConfig.instance
            .searchUsers(query)
            config.enqueue(object : Callback<MainResponse> {
                override fun onResponse(
                    call: Call<MainResponse>,
                    response: Response<MainResponse>
                ) {
                    if (response.isSuccessful) {
                        _users.postValue(response.body()?.items)
                    }
                }

                override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }

            })
    }

    fun users(): LiveData<ArrayList<User>> {
        return _users
    }

}