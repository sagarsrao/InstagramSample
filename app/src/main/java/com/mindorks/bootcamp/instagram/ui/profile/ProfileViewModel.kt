package com.mindorks.bootcamp.instagram.ui.profile

import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class ProfileViewModel(schedulerProvider: SchedulerProvider,
                       compositeDisposable: CompositeDisposable,
                       networkHelper: NetworkHelper,
                       private val userRepository: UserRepository
): BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {
    override fun onCreate() {

    }
}