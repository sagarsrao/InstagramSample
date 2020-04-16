package com.mindorks.bootcamp.instagram.data.model.uploadphoto

import com.google.gson.annotations.SerializedName

data class ResponseUploadPhoto(

	@field:SerializedName("data")
	val data: DataUploadImage? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)