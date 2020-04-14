package com.mindorks.bootcamp.instagram.data.repository

import com.mindorks.bootcamp.instagram.data.local.db.DatabaseService
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.model.allPost.DataItem
import com.mindorks.bootcamp.instagram.data.model.editProfile.EditProfileRequest
import com.mindorks.bootcamp.instagram.data.model.editProfile.ResponseEditMyProfile

import com.mindorks.bootcamp.instagram.data.remote.NetworkService
import com.mindorks.bootcamp.instagram.data.remote.Networking

import io.reactivex.Single
import javax.inject.Inject


class EditProfileRepository @Inject constructor(
    private val networkService: NetworkService,
    private val databaseService: DatabaseService
) {


    fun saveEditProfileInformation(
        name: String?,
        profilePicURL: String?,
        tagline: String?,
        user: User
    ): Single<ResponseEditMyProfile> {
        return networkService.saveEditMyProfileInfo(
            EditProfileRequest(name!!, profilePicURL!!, tagline!!),
            Networking.API_KEY,
            user.accessToken, user.id
        ).map { it }
    }


}