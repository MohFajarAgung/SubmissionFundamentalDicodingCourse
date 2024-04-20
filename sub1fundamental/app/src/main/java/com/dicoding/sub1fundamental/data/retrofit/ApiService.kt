package com.dicoding.sub1fundamental.data.retrofit

import com.dicoding.sub1fundamental.data.response.DetailUserResoponse
import com.dicoding.sub1fundamental.data.response.GithubResponse
import com.dicoding.sub1fundamental.data.response.ItemsItem

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUser(
        @Query("q") id: String
    ): Call<GithubResponse>
    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResoponse>
    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>
    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}


