package com.mindorks.bootcamp.instagram.data.model.createposts

import com.google.gson.annotations.SerializedName
import java.util.*

data class CreatePostResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Data(

	@field:SerializedName("imgUrl")
	val imgUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: Date? = null,

	@field:SerializedName("imgWidth")
	val imgWidth: Int? = null,

	@field:SerializedName("imgHeight")
	val imgHeight: Int? = null,

	@field:SerializedName("id")
	val id: String? = null
)
