package com.mindorks.bootcamp.instagram.data.model.getProfile

import com.google.gson.annotations.SerializedName

data class ResGetProfile(

    @field:SerializedName("data")
	val data: DataGetProfile? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("statusCode")
	val statusCode: String? = null,

    @field:SerializedName("status")
	val status: Int? = null
)