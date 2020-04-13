package com.mindorks.bootcamp.instagram.data.model.allPost

import com.google.gson.annotations.SerializedName
import com.mindorks.bootcamp.instagram.data.model.allPost.DataItem

data class PostListResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)