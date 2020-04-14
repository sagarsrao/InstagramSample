package com.mindorks.bootcamp.instagram.data.model.editProfile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseEditMyProfile(
    @Expose
    @SerializedName("statusCode")
    val statusCode: String,

    @Expose
    @SerializedName("status")
    val status: String,

    @Expose
    @SerializedName("message")
    val message: String
)