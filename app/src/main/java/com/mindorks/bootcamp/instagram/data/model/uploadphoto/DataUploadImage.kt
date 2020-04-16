package com.mindorks.bootcamp.instagram.data.model.uploadphoto

import com.google.gson.annotations.SerializedName

data class DataUploadImage(

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null
)