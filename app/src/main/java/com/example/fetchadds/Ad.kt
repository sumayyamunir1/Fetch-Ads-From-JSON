package com.example.fetchadds

import com.google.gson.annotations.SerializedName

data class Ad(
    @SerializedName("items") var items: ArrayList<Items> = arrayListOf(),
    @SerializedName("fetchMore") var fetchMore: ArrayList<String> = arrayListOf(),
    @SerializedName("size") var size: Int? = null,
    @SerializedName("version") var version: String? = null,
)

data class Items(

    @SerializedName("description") var description: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("ad-type") var ad_type: String? = null,
    @SerializedName("location") var location: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("price") var price: Price? = Price(),
    @SerializedName("image") var image: Image? = Image(),
    @SerializedName("score") var score: Double? = null,
    @SerializedName("version") var version: String? = null,
    @SerializedName("favourite") var favourite: Favourite? = Favourite(),
    var imageUrl:String?=null

)

data class Price(

    @SerializedName("value") var value: Int? = null

)

data class Image(

    @SerializedName("url") var url: String? = null,
    @SerializedName("height") var height: Int? = null,
    @SerializedName("width") var width: Int? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("scalable") var scalable: Boolean? = null

)

data class Favourite(

    @SerializedName("itemId") var itemId: String? = null,
    @SerializedName("itemType") var itemType: String? = null,
    @SerializedName("isFavorite") var isFavorite: Boolean = false

)