package com.mindorks.bootcamp.instagram.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
import com.mindorks.bootcamp.instagram.ui.dummy.DummyActivity
import com.mindorks.bootcamp.instagram.ui.main.MainActivity
import com.mindorks.bootcamp.instagram.ui.signup.SignUpActivity
import com.mindorks.bootcamp.instagram.utils.common.Event
import com.mindorks.bootcamp.instagram.utils.common.Status
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.et_email
import kotlinx.android.synthetic.main.activity_sign_up.*

class LoginActivity : BaseActivity<LoginViewModel>() {

    companion object {
        const val TAG = "LoginActivity"
        const val DRAWABLE_LEFT = 0;
        const val DRAWABLE_TOP = 1;
        const val DRAWABLE_RIGHT = 2;
        const val DRAWABLE_BOTTOM = 3;
    }

    override fun provideLayoutId(): Int = R.layout.activity_login

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {

        et_email.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onEmailChange(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        et_password.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onPasswordChange(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        bt_login.setOnClickListener { viewModel.onLogin() }

        tv_sign_up_with_email.setOnClickListener {

            startActivity(Intent(this@LoginActivity,SignUpActivity::class.java))
        }

        et_email.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if(event?.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (et_email.getRight() - et_email.getCompoundDrawables()[SignUpActivity.DRAWABLE_RIGHT].getBounds().width())) {
                        if(et_email.text.toString().isNotEmpty()){
                            et_email.text?.clear()
                        }

                        return true;
                    }
                }
                return false;
            }

        })

        et_password.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if(event?.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (et_password.getRight() - et_password.getCompoundDrawables()[SignUpActivity.DRAWABLE_RIGHT].getBounds().width())) {
                        if(et_password.text.toString().isNotEmpty()){
                            et_password.text?.clear()
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
        // Event is used by the view model to tell the activity to launch another activity
        // view model also provided the Bundle in the event that is needed for the Activity
        viewModel.launchDummy.observe(this, Observer<Event<Map<String, String>>> {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        })

        viewModel.emailField.observe(this, Observer {
            if (et_email.text.toString() != it) et_email.setText(it)
        })

        viewModel.emailValidation.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> layout_email.error = it.data?.run { getString(this) }
                else -> layout_email.isErrorEnabled = false
            }
        })

        viewModel.passwordField.observe(this, Observer {
            if (et_password.text.toString() != it) et_email.setText(it)
        })

        viewModel.passwordValidation.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> layout_password.error = it.data?.run { getString(this) }
                else -> layout_password.isErrorEnabled = false
            }
        })

        viewModel.loggingIn.observe(this, Observer {
            pb_loading.visibility = if (it) View.VISIBLE else View.GONE
        })




    }
}