package com.mindorks.bootcamp.instagram.data.remote

import com.mindorks.bootcamp.instagram.data.model.allPost.PostListResponse
import com.mindorks.bootcamp.instagram.data.model.createposts.CreatePostResponse
import com.mindorks.bootcamp.instagram.data.model.createposts.RequestCreatePost
import com.mindorks.bootcamp.instagram.data.model.editProfile.EditProfileRequest
import com.mindorks.bootcamp.instagram.data.model.editProfile.ResponseEditMyProfile
import com.mindorks.bootcamp.instagram.data.model.getProfile.ResGetProfile
import com.mindorks.bootcamp.instagram.data.model.like.PostLikeResponse
import com.mindorks.bootcamp.instagram.data.model.logout.ResLogOut
import com.mindorks.bootcamp.instagram.data.model.unlike.PostUnlikeResponse
import com.mindorks.bootcamp.instagram.data.model.uploadphoto.ResponseUploadPhoto
import com.mindorks.bootcamp.instagram.data.remote.request.DummyRequest
import com.mindorks.bootcamp.instagram.data.remote.request.LoginRequest
import com.mindorks.bootcamp.instagram.data.remote.request.PostLikeRequest
import com.mindorks.bootcamp.instagram.data.remote.request.SignUpRequest
import com.mindorks.bootcamp.instagram.data.remote.response.DummyResponse
import com.mindorks.bootcamp.instagram.data.remote.response.SignUpAndLoginResponse
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*
import javax.inject.Singleton

@Singleton
interface NetworkService {


    @POST(Endpoints.DUMMY)
    fun doDummyCall(
        @Body request: DummyRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY // default value set when Networking create is called
    ): Single<DummyResponse>

    /*
     * Example to add other headers
     *
     *  @POST(Endpoints.DUMMY_PROTECTED)
        fun sampleDummyProtectedCall(
            @Body request: DummyRequest,
            @Header(Networking.HEADER_USER_ID) userId: String, // pass using UserRepository
            @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String, // pass using UserRepository
            @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY // default value set when Networking create is called
        ): Single<DummyResponse>
     */

    @POST(Endpoints.LOGIN)
    fun doLoginCall(
        @Body request: LoginRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): Single<SignUpAndLoginResponse>

    @POST(Endpoints.SIGNUP)
    fun doSignUp(
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY,
        @Body request: SignUpRequest
    ): Single<SignUpAndLoginResponse>

    @GET(Endpoints.ALL_POSTS)
    fun doHomePostListCall(
        @Query("firstPostId") firstPostId: String?,
        @Query("lastPostId") lastPostId: String?,
        @Header(Networking.HEADER_USER_ID) userId: String,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): Single<PostListResponse>


    @PUT(Endpoints.POST_LIKE)
    fun hitLikeButtonList(
        @Body body: PostLikeRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_USER_ID) userId: String
    ): Single<PostLikeResponse>


    @PUT(Endpoints.POST_UNLIKE)
    fun hitUnLikeButtonList(
        @Body body: PostLikeRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_USER_ID) userId: String
    ): Single<PostUnlikeResponse>


    @PUT(Endpoints.EDIT_MY_PROFILE)
    fun saveEditMyProfileInfo(
        @Body body: EditProfileRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_USER_ID) userId: String
    ): Single<ResponseEditMyProfile>

    @Multipart
    @POST(Endpoints.IMAGE_UPLOAD)
    fun pushPhotoToServer(
        @Part file: MultipartBody.Part,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_USER_ID) userId: String
    ): Observable<ResponseUploadPhoto>


    @GET(Endpoints.FETCH_MY_INFO)
    fun doGetMyPostInfo(
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_USER_ID) userId: String
    ): Observable<ResGetProfile>


    @DELETE(Endpoints.LOGOUT)
    fun exit(
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_USER_ID) userId: String
    ): Single<ResLogOut>

    @POST(Endpoints.POST_CREATION)
    fun postCreate(
        @Body body: RequestCreatePost,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_USER_ID) userId: String
    ): Single<CreatePostResponse>
}