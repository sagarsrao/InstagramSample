package com.mindorks.bootcamp.instagram.data.model.allPost

import com.google.gson.annotations.SerializedName
import java.util.*

data class DataItem(

    @field:SerializedName("imgUrl")
    val imgUrl: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: Date,

    @field:SerializedName("imgWidth")
    val imgWidth: Int? = null,

    @field:SerializedName("likedBy")
    val likedBy: MutableList<LikedByItem?>? = null,

    @field:SerializedName("imgHeight")
    val imgHeight: Int? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("user")
    val user: LikedByItem? = null
){
	data class LikedByItem(

		@field:SerializedName("profilePicUrl")
		val profilePicUrl: String? = null,

		@field:SerializedName("name")
		val name: String? = null,

		@field:SerializedName("id")
		val id: String? = null
	)

}