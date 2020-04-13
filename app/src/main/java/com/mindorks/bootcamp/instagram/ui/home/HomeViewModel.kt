package com.mindorks.bootcamp.instagram.ui.home

import androidx.lifecycle.MutableLiveData
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.model.allPost.DataItem
import com.mindorks.bootcamp.instagram.data.repository.PostRepository
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.Resource
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers

class HomeViewModel(schedulerProvider: SchedulerProvider,
                    compositeDisposable: CompositeDisposable,
                    networkHelper: NetworkHelper,
                    private val userRepository: UserRepository,
                    private val postRepository: PostRepository,
                    private val allPostList: ArrayList<DataItem>,
                    private val paginator: PublishProcessor<Pair<String?, String?>>
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {


    /*Publish processor :- Its a kind of hot observable and can be used multiple times and its suitable for the scenario's like pagination*/
    val loading:MutableLiveData<Boolean> = MutableLiveData() // This is for the spinner
    val posts:MutableLiveData<Resource<List<DataItem>>> = MutableLiveData() // loading the data from the api


    private val user: User = userRepository.getCurrentUser()!!//This should not be used without login
    override fun onCreate() {

        loadMorePosts()
    }


    init {
        compositeDisposable.add(
            paginator
                .onBackpressureDrop() /*This will work like if there are 10 items in a list and on Scroll we get another 10 items so old items will be replaced by the new one*/
                .doOnNext {
                    loading.postValue(true)
                }
                .concatMapSingle { pageIds ->
                    /*This operator helps in running singleSequence of operation */
                    return@concatMapSingle postRepository

                        .fetchHomePostList(pageIds.first, pageIds.second, user)
                        .subscribeOn(Schedulers.io())
                            /*It is handled here because there is lot of chance of API can fail and if so paginator will fail for sure*/
                        .doOnError {
                            handleNetworkError(it)
                        }
                }
                .subscribe(
                    {

                        allPostList.addAll(it) //get all the results
                        loading.postValue(false)

                        posts.postValue(Resource.success(it))
                    },
                    {
                        handleNetworkError(it)
                    }
                )
        )
    }

    private fun loadMorePosts() {
        val firstPostId = if (allPostList.isNotEmpty()) allPostList[0].id else null //first position of the list
        val lastPostId = if (allPostList.size > 1) allPostList[allPostList.size - 1].id else null // last position of the list
        if (checkInternetConnectionWithMessage()) paginator.onNext(Pair(firstPostId, lastPostId))
    }


    /*The below function helps like whether the user is scrolling from bottom at that time
    * we will call and hit the api again*/
    fun onLoadMore() {
        if (loading.value !== null && loading.value == false) loadMorePosts()
    }
}