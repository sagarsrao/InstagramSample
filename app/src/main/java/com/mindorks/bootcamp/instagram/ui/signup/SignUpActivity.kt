package com.mindorks.bootcamp.instagram.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
import com.mindorks.bootcamp.instagram.ui.login.LoginActivity
import com.mindorks.bootcamp.instagram.utils.common.Status
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.et_email

class SignUpActivity : BaseActivity<SignUpViewModel>() {


    companion object {

        const val TAG = "SignUpActivity"
    }

    override fun provideLayoutId(): Int {
        return R.layout.activity_sign_up
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {

        tv_login_with_email.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            finish()
        }

        /*Add text change Listeners for every field*/
        ed_user_full_name.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onUserNameChange(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        et_email.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onEmailChange(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        et_password_sign_up.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onPasswordChange(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        bt_sign_up.setOnClickListener { viewModel.onSignUp() }


    }

    override fun setupObservers() {
        super.setupObservers()


        /*Observing single each fields that can be used for the validations*/
        /*USER NAME*/
        viewModel.userNameField.observe(this, Observer {
            if (ed_user_full_name.text.toString() != it) ed_user_full_name.setText(it)
        })

        viewModel.userNameValidation.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> layout_user_full_name.error = it.data?.run { getString(this) }
                else -> layout_user_full_name.isErrorEnabled = false
            }
        })


        /*EMAIL*/
        viewModel.emailField.observe(this, Observer {
            if (et_email.text.toString() != it) et_email.setText(it)
        })

        viewModel.emailValidation.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> layout_email_sign_up.error = it.data?.run { getString(this) }
                else -> layout_email_sign_up.isErrorEnabled = false
            }

        })

        /*PASSWORD*/

        viewModel.passwordField.observe(this, Observer {
            if (et_password_sign_up.text.toString() != it) et_password_sign_up.setText(it)
        })

        viewModel.passwordValidation.observe(this, Observer {

            when (it.status) {

                Status.ERROR -> layout_password_sign_up.error = it.data?.run { getString(this) }
                else -> layout_password_sign_up.isErrorEnabled = false

            }

        })
    }
}
