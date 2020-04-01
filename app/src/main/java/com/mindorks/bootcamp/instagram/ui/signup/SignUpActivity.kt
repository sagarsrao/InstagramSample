package com.mindorks.bootcamp.instagram.ui.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity

class SignUpActivity :BaseActivity<SignUpViewModel>() {



    override fun provideLayoutId(): Int {
        return  R.layout.activity_sign_up
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {

    }
}
