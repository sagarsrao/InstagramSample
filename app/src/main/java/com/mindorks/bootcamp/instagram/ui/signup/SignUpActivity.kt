package com.mindorks.bootcamp.instagram.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
import com.mindorks.bootcamp.instagram.ui.login.LoginActivity
import com.mindorks.bootcamp.instagram.utils.common.Event
import com.mindorks.bootcamp.instagram.utils.common.Status
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.et_email

class SignUpActivity : BaseActivity<SignUpViewModel>() {


    companion object {

        const val TAG = "SignUpActivity"
        const val DRAWABLE_LEFT = 0;
        const val DRAWABLE_TOP = 1;
        const val DRAWABLE_RIGHT = 2;
        const val DRAWABLE_BOTTOM = 3;
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

        /*On Click of full user name close symbol*/
        ed_user_full_name.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if(event?.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (ed_user_full_name.getRight() - ed_user_full_name.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if(ed_user_full_name.text.toString().isNotEmpty()){
                            ed_user_full_name.text?.clear()
                        }

                        return true;
                    }
                }
                return false;
            }

        })

        et_email.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if(event?.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (et_email.getRight() - et_email.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if(et_email.text.toString().isNotEmpty()){
                            et_email.text?.clear()
                        }

                        return true;
                    }
                }
                return false;
            }

        })

        et_password_sign_up.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if(event?.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (et_password_sign_up.getRight() - et_password_sign_up.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if(et_password_sign_up.text.toString().isNotEmpty()){
                            et_password_sign_up.text?.clear()
                        }

                        return true;
                    }
                }
                return false;
            }

        })








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


        /*In the view model we create a mutableLiveData*/
        /*to hold the data*/
        /*we hold the data and update it to the activity using setValue and postValue*/
        /*Inside the activity we just update the activity by observing it from the view model
        * provided it also notifies*/
        viewModel.launchLoginScreen.observe(this, Observer<Event<Map<String,String>>>{

            it.getIfNotHandled().run {
                startActivity(Intent(this@SignUpActivity,LoginActivity::class.java))
                finish()

            }


        })

        viewModel.signingUp.observe(this, Observer {
            pb_loading_sign_up.visibility = if (it) View.VISIBLE else View.GONE
        })
    }
}
