package com.mindorks.bootcamp.instagram.ui.editProfile

import android.app.ProgressDialog
import android.os.Bundle
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity


class EditProfileActivity : BaseActivity<EditProfileActivityViewModel>() {


    lateinit var mProgressDialog: ProgressDialog


    override fun provideLayoutId(): Int {
        return R.layout.activity_edit_profile
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {




    }


    private fun showProgressDialog() {
        mProgressDialog = ProgressDialog(this,R.style.MyAlertDialogStyle)
        mProgressDialog.setMessage(getString(R.string.progress_message))
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)

        mProgressDialog.show()
        mProgressDialog.setCancelable(false)
        Thread(Runnable {
            try {
                Thread.sleep(10000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            mProgressDialog.dismiss()
        }).start()
    }
}
