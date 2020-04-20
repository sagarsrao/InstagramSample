package com.mindorks.bootcamp.instagram.data.repository

import com.mindorks.bootcamp.instagram.data.local.db.DatabaseService
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.model.allPost.DataItem
import com.mindorks.bootcamp.instagram.data.model.createposts.CreatePostResponse
import com.mindorks.bootcamp.instagram.data.model.createposts.RequestCreatePost
import com.mindorks.bootcamp.instagram.data.model.editProfile.EditProfileRequest
import com.mindorks.bootcamp.instagram.data.model.editProfile.ResponseEditMyProfile
import com.mindorks.bootcamp.instagram.data.model.getProfile.ResGetProfile
import com.mindorks.bootcamp.instagram.data.model.logout.ResLogOut
import com.mindorks.bootcamp.instagram.data.model.uploadphoto.ResponseUploadPhoto

import com.mindorks.bootcamp.instagram.data.remote.NetworkService
import com.mindorks.bootcamp.instagram.data.remote.Networking
import io.reactivex.Observable

import io.reactivex.Single
import okhttp3.MultipartBody
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


    fun uploadPhotoAttached(part: MultipartBody.Part, user: User): Observable<ResponseUploadPhoto> {
        return networkService.pushPhotoToServer(
            part,
            Networking.API_KEY, user.accessToken, user.id
        )
    }


    fun getProfileInfo(user: User): Observable<ResGetProfile> {
        return networkService.doGetMyPostInfo(Networking.API_KEY, user.accessToken, user.id)
    }

    fun logout(user: User): Single<ResLogOut> {
        return networkService.exit(Networking.API_KEY, user.accessToken, user.id)
    }


    fun createPostAfterPhotoTaken(imageUrl:String,user: User):Single<CreatePostResponse>{
        return networkService.postCreate(RequestCreatePost(imageUrl,800,600),Networking.API_KEY,user.accessToken,user.id)
    }


}