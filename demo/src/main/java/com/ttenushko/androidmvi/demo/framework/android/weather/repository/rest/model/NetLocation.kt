package com.ttenushko.androidmvi.demo.framework.android.weather.repository.rest.model

import com.google.gson.annotations.SerializedName

data class NetLocation(
    @field:SerializedName("lat") val latitude: Float,
    @field:SerializedName("lon") val longitude: Float
)