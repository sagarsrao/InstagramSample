package com.mindorks.bootcamp.instagram.data.model.createposts

import com.google.gson.annotations.SerializedName

data class RequestCreatePost(

	@field:SerializedName("imgUrl")
	val imgUrl: String? = null,

	@field:SerializedName("imgWidth")
	val imgWidth: Int? = null,

	@field:SerializedName("imgHeight")
	val imgHeight: Int? = null
)
