package com.mindorks.bootcamp.instagram.data.model.editProfile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EditProfileRequest(
    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("profilePicUrl")
    @Expose
    val profilePicUrl: String,


    @SerializedName("tagline")
    @Expose
    val tagline: String
) {
}