package com.mindorks.bootcamp.instagram.ui.home.posts

import android.media.Image
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.mindorks.bootcamp.instagram.R

import com.mindorks.bootcamp.instagram.data.model.allPost.DataItem
import com.mindorks.bootcamp.instagram.data.remote.Networking
import com.mindorks.bootcamp.instagram.data.repository.PostRepository
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseItemViewModel
import com.mindorks.bootcamp.instagram.utils.common.Resource
import com.mindorks.bootcamp.instagram.utils.common.TimeUtils
import com.mindorks.bootcamp.instagram.utils.display.ScreenUtils
import com.mindorks.bootcamp.instagram.utils.log.Logger
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PostsItemViewModel @Inject constructor(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    userRepository: UserRepository,
    private val postRepository: PostRepository
) : BaseItemViewModel<DataItem>(schedulerProvider, compositeDisposable, networkHelper) {

    companion object {
        const val TAG = "PostItemViewModel"
    }

    private val user = userRepository.getCurrentUser()!!
    private val screenWidth = ScreenUtils.getScreenWidth()
    private val screenHeight = ScreenUtils.getScreenHeight()
    private val headers = mapOf(
        Pair(Networking.HEADER_API_KEY, Networking.API_KEY),
        Pair(Networking.HEADER_USER_ID, user.id),
        Pair(Networking.HEADER_ACCESS_TOKEN, user.accessToken)
    )

    val name: LiveData<String> = Transformations.map(data) { it.user?.name }
    val postTime: LiveData<String> = Transformations.map(data) {
        TimeUtils.getTimeAgo(it.createdAt)
    }
    val likesCount: LiveData<Int> = Transformations.map(data) { it.likedBy?.size ?: 0 }
    val isLiked: LiveData<Boolean> = Transformations.map(data) {
        it.likedBy?.find { postUser -> postUser?.id == user.id } !== null
    }

    val profileImage: LiveData<com.mindorks.bootcamp.instagram.utils.display.Image> = Transformations.map(data) {
        it.user?.profilePicUrl?.run {
            com.mindorks.bootcamp.instagram.utils.display.Image(this,headers)
        }
    }

    val imageDetail: LiveData<com.mindorks.bootcamp.instagram.utils.display.Image>? = Transformations.map(data) {
        com.mindorks.bootcamp.instagram.utils.display.Image(
            it.imgUrl!!,
            headers,
            screenWidth,
            it.imgHeight?.let { height ->
                return@let (calculateScaleFactor(it) * height).toInt()
            } ?: screenHeight / 3)
    }

    override fun onCreate() {
        Logger.d(TAG, "onCreate called")
    }

    private fun calculateScaleFactor(post: DataItem) =
        post.imgWidth?.let { return@let screenWidth.toFloat() / it } ?: 1f

    fun onLikeClick() = data.value?.let {
        if (networkHelper.isNetworkConnected()) {
            val api =
                if (isLiked.value == true)
                    postRepository.doHitUnLikeButton(it, user)
                else
                    postRepository.doHitLikeButton(it, user)

            compositeDisposable.add(api
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { responsePost -> if (responsePost.id == it.id) updateData(responsePost) },
                    { error -> handleNetworkError(error) }
                )
            )
        } else {
            messageStringId.postValue(Resource.error(R.string.network_connection_error))
        }
    }

}