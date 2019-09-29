package com.example.testoftest.network

import com.example.testoftest.dashboard.pojo.Gifs
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface APIServices {

    /**
     * define your get/post api here
     */

    @GET("/v1/gifs/search")
    fun gifsinfo (@QueryMap params: Map<String, String>): Call<Gifs>
}