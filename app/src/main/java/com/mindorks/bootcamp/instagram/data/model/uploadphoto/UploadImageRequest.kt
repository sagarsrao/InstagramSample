package com.mindorks.bootcamp.instagram.data.model.uploadphoto

import com.google.gson.annotations.SerializedName

data class UploadImageRequest(

	@field:SerializedName("image")
	val image: String? = null
)