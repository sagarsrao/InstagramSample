package com.mindorks.bootcamp.instagram.utils.common

import com.mindorks.bootcamp.instagram.R
import java.util.regex.Pattern

object Validator {

     val EMAIL_ADDRESS = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

     const val MIN_PASSWORD_LENGTH = 6

    fun validateLoginFields(email: String?, password: String?): List<Validation> =
        ArrayList<Validation>().apply {
            when {
                email.isNullOrBlank() ->
                    add(Validation(Validation.Field.EMAIL, Resource.error(R.string.email_field_empty)))
                !EMAIL_ADDRESS.matcher(email).matches() ->
                    add(Validation(Validation.Field.EMAIL, Resource.error(R.string.email_field_invalid)))
                else ->
                    add(Validation(Validation.Field.EMAIL, Resource.success()))
            }
            when {
                password.isNullOrBlank() ->
                    add(Validation(Validation.Field.PASSWORD, Resource.error(R.string.password_field_empty)))
                password.length < MIN_PASSWORD_LENGTH ->
                    add(Validation(Validation.Field.PASSWORD, Resource.error(R.string.password_field_small_length)))
                else -> add(Validation(Validation.Field.PASSWORD, Resource.success()))
            }
        }


    fun validateSignUpFields(userName:String?,email: String?, password: String?): List<Validation> =
        ArrayList<Validation>().apply {
            when {
                email.isNullOrBlank() ->
                    add(Validation(Validation.Field.EMAIL, Resource.error(R.string.email_field_empty)))
                !Validator.EMAIL_ADDRESS.matcher(email).matches() ->
                    add(Validation(Validation.Field.EMAIL, Resource.error(R.string.email_field_invalid)))
                else ->
                    add(Validation(Validation.Field.EMAIL, Resource.success()))
            }
            when {
                password.isNullOrBlank() ->
                    add(Validation(Validation.Field.PASSWORD, Resource.error(R.string.password_field_empty)))
                password.length < Validator.MIN_PASSWORD_LENGTH ->
                    add(Validation(Validation.Field.PASSWORD, Resource.error(R.string.password_field_small_length)))
                else -> add(Validation(Validation.Field.PASSWORD, Resource.success()))
            }
            when{
                userName.isNullOrBlank() ->
                    add(Validation(Validation.Field.USERNAME,Resource.error(R.string.user_name_field_hint_empty)))
                else ->
                    add(Validation(Validation.Field.USERNAME, Resource.success()))
            }

        }

}



data class Validation(val field: Field, val resource: Resource<Int>) {

    enum class Field {
        EMAIL,
        PASSWORD,
        USERNAME
    }
}
