package com.mindorks.bootcamp.instagram.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.*
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class MainViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper

) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {


     val switchToHome = MutableLiveData<Event<Boolean>>()
     val switchToPhotos = MutableLiveData<Event<Boolean>>()
     val switchToProfile = MutableLiveData<Event<Boolean>>()
    override fun onCreate() {

        switchToHome.postValue(Event(true))
    }

    fun homeClicked(){
        switchToHome.postValue(Event(true))
    }

    fun photosClicked(){
        switchToPhotos.postValue(Event(true))
    }

    fun profileClicked(){
        switchToProfile.postValue(Event(true))
    }

}