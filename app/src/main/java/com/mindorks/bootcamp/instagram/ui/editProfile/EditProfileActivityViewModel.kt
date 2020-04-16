package com.mindorks.bootcamp.instagram.ui.editProfile


import androidx.lifecycle.MutableLiveData
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.model.uploadphoto.UploadImageRequest
import com.mindorks.bootcamp.instagram.data.repository.EditProfileRepository
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.Event
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import java.io.File


class EditProfileActivityViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    userRepository: UserRepository,
    private val editProfileRepository: EditProfileRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val savingData: MutableLiveData<Boolean> = MutableLiveData()
    val editUserName: MutableLiveData<String> = MutableLiveData()
    val editBio: MutableLiveData<String> = MutableLiveData()
    val successImageAttached: MutableLiveData<String> = MutableLiveData()

    val launchProfileFragment: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()
    val profilePicUrl: MutableLiveData<String> = MutableLiveData()

    private val user: User =
        userRepository.getCurrentUser()!!//This should not be used without login


    override fun onCreate() {

    }

    fun onEditNameChange(name: String) = editUserName.postValue(name)
    fun onBioEditChange(bio: String) = editBio.postValue(bio)
    fun onImageUrlSave(imageUrl: String) = profilePicUrl.postValue(imageUrl)

    fun onSave() {

        val name = editUserName.value
        val editBio = editBio.value
        val profilePicUrl = profilePicUrl.value

        if (checkInternetConnectionWithMessage()) {
            savingData.postValue(true)
            compositeDisposable.addAll(
                editProfileRepository.saveEditProfileInformation(name, profilePicUrl, editBio, user)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        savingData.postValue(false) // progress bar
                        launchProfileFragment.postValue(Event(emptyMap()))

                    }, {
                        handleNetworkError(it)
                        savingData.postValue(false) // progress bar

                    })
            )

        }

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