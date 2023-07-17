package com.example.fetchadds

import retrofit2.Call
import retrofit2.http.GET

interface AdApiService {
    // defines the API endpoints for fetching ads
    @GET("baldermork/6a1bcc8f429dcdb8f9196e917e5138bd/raw/discover.json")
    fun getAds(): Call<Ad>
}
