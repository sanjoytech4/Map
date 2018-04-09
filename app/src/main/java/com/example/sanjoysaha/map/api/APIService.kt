package com.example.sanjoysaha.map.api


import com.example.sanjoysaha.map.model.LocationData
import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.GET
import java.util.*


interface APIService {
    companion object {
        val BASE_URL:String = "http://192.168.59.6:8080"
    }

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("/explore")
    fun getLocation(): Call<LocationData>
}