package com.mindorks.bootcamp.instagram.ui.photo

import androidx.lifecycle.MutableLiveData
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.repository.EditProfileRepository
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class PhotoViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository,
    private val editProfileRepository: EditProfileRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val savingData: MutableLiveData<Boolean> = MutableLiveData()
    private val user: User =
        userRepository.getCurrentUser()!!//This should not be used without login
    val successImageAttached: MutableLiveData<String> = MutableLiveData()


    override fun onCreate() {

    }


    fun doUploadImage(filename: String) {
        val image = filename
        var profilePic: MultipartBody.Part? = null
        if (null != image) {
            val file = File(image)
            val reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            profilePic = MultipartBody.Part.createFormData("image", file.name, reqFile)
        }
        if (checkInternetConnectionWithMessage()) {
            savingData.postValue(true)
            compositeDisposable.addAll(
                editProfileRepository.uploadPhotoAttached(profilePic!!, user)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        savingData.postValue(false) // progress bar
                        successImageAttached.postValue(it.data?.imageUrl)

                    }, {
                        handleNetworkError(it)
                        savingData.postValue(false) // progress bar


                    })
            )

        }


    }


}