package com.mindorks.bootcamp.instagram.data.repository

import com.mindorks.bootcamp.instagram.data.local.db.DatabaseService
import com.mindorks.bootcamp.instagram.data.local.prefs.UserPreferences
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.model.allPost.DataItem
import com.mindorks.bootcamp.instagram.data.model.allPost.PostListResponse
import com.mindorks.bootcamp.instagram.data.model.like.PostLikeResponse
import com.mindorks.bootcamp.instagram.data.model.unlike.PostUnlikeResponse
import com.mindorks.bootcamp.instagram.data.remote.NetworkService
import com.mindorks.bootcamp.instagram.data.remote.Networking
import com.mindorks.bootcamp.instagram.data.remote.request.LoginRequest
import com.mindorks.bootcamp.instagram.data.remote.request.PostLikeRequest
import com.mindorks.bootcamp.instagram.data.remote.request.SignUpRequest
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton



class PostRepository @Inject constructor(
    private val networkService: NetworkService,
    private val databaseService: DatabaseService
) {


    fun fetchHomePostList(firstPostId: String?, lastPostId: String?, user: User): Single<List<DataItem>> {
        return networkService.doHomePostListCall(
            firstPostId,
            lastPostId,
            user.id,
            user.accessToken
        ).map { it.data }
    }

    fun doHitLikeButton(dataItem: DataItem, user: User): Single<DataItem> =
        networkService.hitLikeButtonList(
            PostLikeRequest(dataItem.id!!),
            Networking.API_KEY,
            user.accessToken,
            user.id
        ).map {
            dataItem.likedBy?.apply {

                /*here we are comparing the Liked user details with the actual user details who has
                * logged in */
                this.find { it!!.id == user.id } ?: this.add(
                    DataItem.LikedByItem(user.profilePicUrl, user.name, user.id)
                )


            }
            return@map dataItem
        }


    fun doHitUnLikeButton(dataItem: DataItem, user: User): Single<DataItem> =
        networkService.hitUnLikeButtonList(
            PostLikeRequest(dataItem.id!!),
            Networking.API_KEY,
            user.accessToken,
            user.id
        ).map {

            dataItem.likedBy.apply {
                this?.find { it!!.id == user.id }?.let { this.remove(it) }

            }
            return@map dataItem
        }


}