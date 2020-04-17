package com.mindorks.bootcamp.instagram.data.model.getProfile

import com.google.gson.annotations.SerializedName

data class DataGetProfile(

	@field:SerializedName("profilePicUrl")
	val profilePicUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("tagline")
	val tagline: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)