package com.example.fetchadds

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FetchAds : ComponentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adAdapter: AdsAdapterClass
    private var allAds: List<Items?>? = null
    private lateinit var switchButton: Button
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        switchButton = findViewById(R.id.switchButton)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        // used the switch button to switch between All ads and Favorite ads
        switchButton.setOnClickListener {
            toggleList()
        }
        fetchAllAds()
    }
    
    private fun toggleList() {
        // here the function will return true or false depending on whether there are favorite ads available or not.
        val hasFavoriteAds: Boolean = loadFavoriteAds()
        //this condition return false if there is no data in shared preferences
        if (!hasFavoriteAds) {
            //show all ads
            fetchAllAds()
            switchButton.text = "Show Favourite Ads"
            
        } else {
            //show favourite ads offline from database or shared preferences
            switchButton.text = "Show All Ads"
            //this function is responsible for loading and updating the list of favorite ads from the stored data
            loadFavoriteAds()
            
        }
    }
    
    private fun fetchAllAds() {
        val retrofit = Retrofit.Builder().baseUrl("https://gist.githubusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        
        val adApiService = retrofit.create(AdApiService::class.java)
        val call = adApiService.getAds()
        
        call.enqueue(object : Callback<Ad> {
            override fun onResponse(call: Call<Ad>, response: Response<Ad>) {
                if (response.isSuccessful) {
                    allAds = response.body()?.items ?: emptyList()
                    for (items in allAds!!) {
                        items?.imageUrl =
                            "https://images.finncdn.no/dynamic/480x360c/" + items?.image?.url
                    }
                    adAdapter = AdsAdapterClass(allAds, ::toggleFavoriteAd)
                    adAdapter.notifyDataSetChanged()
                    recyclerView.adapter = adAdapter
                    
                } else {
                    // Handle error response
                    Toast.makeText(
                        this@FetchAds, "Failed to fetch ads from JSON", Toast.LENGTH_SHORT
                    ).show()
                }
            }
            
            override fun onFailure(call: Call<Ad>, t: Throwable) {
                // Handle network failure
                Toast.makeText(this@FetchAds, "There is a Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    private fun toggleFavoriteAd(position: Int) {
        val items = allAds?.get(position)
        if (items != null) {
            items.favourite?.itemId = items.id
            items.favourite?.itemType = items.type
        }
        // used this to notify the RecyclerView adapter that the item at the specified position has been changed
        adAdapter.notifyItemChanged(position)
        //Save the favorite items list to local storage (e.g database or shared preferences)
        saveFavoriteAds()
    }
    
    //function is responsible for saving the list of favorite ads to the shared preferences for later retrieval
    private fun saveFavoriteAds() {
        val sharedPreferences = getSharedPreferences(PREF_FAVORITE_ADS, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        if (allAds != null) {
            val favoriteAdsJson = Gson().toJson(allAds!!.filter { it!!.favourite != null })
            editor.putString(PREF_FAVORITE_ADS, favoriteAdsJson)
            editor.apply()
        }
    }
    
    private fun loadFavoriteAds(): Boolean {
        val sharedPreferences = getSharedPreferences(PREF_FAVORITE_ADS, Context.MODE_PRIVATE)
        val favoriteAdsJson = sharedPreferences.getString(PREF_FAVORITE_ADS, null)
        return if (!favoriteAdsJson.isNullOrEmpty()) {
            val favoriteAds = Gson().fromJson<Ad>(favoriteAdsJson, Ad::class.java)
            favoriteAds?.let {
                for (item in favoriteAds.items) {
                    val matchingAd = allAds?.find { it?.id == item.id && it?.type == item.type }
                    matchingAd?.favourite?.itemId = item.favourite?.itemId
                    matchingAd?.favourite?.itemType = item.favourite?.itemType
                    matchingAd?.favourite?.isFavorite = true
                }
                adAdapter.notifyDataSetChanged()
            }
            true // return true if Loading successful
        } else {
            false // No favorite ads stored
        }
    }
    
    companion object {
        private const val PREF_FAVORITE_ADS = "pref_favorite_ads"
    }
}
