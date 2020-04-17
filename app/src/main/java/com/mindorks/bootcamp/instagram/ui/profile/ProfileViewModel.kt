package com.mindorks.bootcamp.instagram.ui.profile

import androidx.lifecycle.MutableLiveData
import com.mindorks.bootcamp.instagram.data.model.getProfile.DataGetProfile
import com.mindorks.bootcamp.instagram.data.repository.EditProfileRepository
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.Event
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class ProfileViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository,
    private val editProfileRepository: EditProfileRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val getInfoComplete: MutableLiveData<DataGetProfile> = MutableLiveData()

    val mProgressInfo: MutableLiveData<Boolean> = MutableLiveData()

    val user = userRepository.getCurrentUser()!!

    val launchLoginScreen: MutableLiveData<Boolean> = MutableLiveData()

    override fun onCreate() {

        getProfileInfo()
    }

    fun getProfileInfo() {

        if (checkInternetConnectionWithMessage()) {
            mProgressInfo.postValue(true)
            compositeDisposable.addAll(
                editProfileRepository.getProfileInfo(user)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        mProgressInfo.postValue(false)
                        if (it.data.toString().isNotEmpty()) {
                            getInfoComplete.postValue(it.data)
                        }
                    }, {
                        handleNetworkError(it)
                        mProgressInfo.postValue(false)
                    })
            )

        }


    }


    fun doLogOut() {
        if (checkInternetConnectionWithMessage()) {
            mProgressInfo.postValue(true)
            compositeDisposable.addAll(
                editProfileRepository.logout(user)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        mProgressInfo.postValue(false)
                        launchLoginScreen.postValue(true)
                    }, {
                        handleNetworkError(it)
                        launchLoginScreen.postValue(false)
                    })
            )

        }


    }
}