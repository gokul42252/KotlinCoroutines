package com.ct.kotlincoroutines.network

import com.ct.kotlincoroutines.data.PlaceholderPhotos
import com.ct.kotlincoroutines.data.PlaceholderPosts
import com.ct.kotlincoroutines.data.PlaceholderUsers
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface PlaceholderApi{

    @GET("/posts")
    fun getPosts() : Deferred<Response<List<PlaceholderPosts>>>

    @GET("/users")
    fun getUsers() : Deferred<Response<List<PlaceholderUsers>>>

    @GET("/photos")
    fun getPhotos() : Deferred<Response<List<PlaceholderPhotos>>>

}