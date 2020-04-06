package com.mindorks.bootcamp.instagram.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.*
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class SignUpViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    private val validationsList: MutableLiveData<List<Validation>> = MutableLiveData()
    val userNameField: MutableLiveData<String> = MutableLiveData()
    val emailField: MutableLiveData<String> = MutableLiveData()
    val passwordField: MutableLiveData<String> = MutableLiveData()
    val signingUp: MutableLiveData<Boolean> = MutableLiveData()

    val launchLoginScreen: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()

    val userNameValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.USERNAME)
    val emailValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.EMAIL)
    val passwordValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.PASSWORD)

    private fun filterValidation(field: Validation.Field) =
        Transformations.map(validationsList) {
            it.find { validation -> validation.field == field }
                ?.run { return@run this.resource }
                ?: Resource.unknown()
        }

    override fun onCreate() {
    }

    fun onEmailChange(email: String) = emailField.postValue(email)
    fun onPasswordChange(password: String) = passwordField.postValue(password)
    fun onUserNameChange(userName: String) = userNameField.postValue(userName)

    fun onSignUp() {
        val email = emailField.value
        val password = passwordField.value
        val userName = userNameField.value

        val validation = Validator.validateSignUpFields(userName, email, password)

        validationsList.postValue(validation)
        if (validation.isNotEmpty() && email != null && password != null && userName != null) {
            val successValidation = validation.filter { it.resource.status == Status.SUCCESS }
            if (successValidation.size == validation.size && checkInternetConnectionWithMessage()) {
                signingUp.postValue(true)
                compositeDisposable.addAll(
                    userRepository.doUserSignUp(userName, email, password)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe({
                            userRepository.saveCurrentUser(it)
                            signingUp.postValue(false)
                            launchLoginScreen.postValue(Event(emptyMap()))


                        }, {
                            handleNetworkError(it)
                            signingUp.postValue(false)

                        })
                )


            }
        }


    }


}