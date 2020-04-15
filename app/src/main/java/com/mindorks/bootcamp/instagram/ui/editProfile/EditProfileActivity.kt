package com.mindorks.bootcamp.instagram.ui.editProfile

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
import com.mindorks.bootcamp.instagram.ui.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_edit_profile.*


class EditProfileActivity : BaseActivity<EditProfileActivityViewModel>() {


    lateinit var mProgressDialog: ProgressDialog


    override fun provideLayoutId(): Int {
        return R.layout.activity_edit_profile
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {

        /*Do all the UI level functionality*/
        ivClose.setOnClickListener {
            goBack()
        }

        ivSaveEditProfile.setOnClickListener {

            viewModel.onSave()
        }

        ed_name.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onEditNameChange(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })


        ed_bio.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onBioEditChange(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })


    }

    override fun goBack() {
        super.goBack()
    }

    override fun setupObservers() {
        super.setupObservers()
        /*Observe all the data coming from ViewModel and do update the activity */

        viewModel.savingData.observe(this, Observer {

            if (it) {
                showProgressDialog(true)
            } else {
                showProgressDialog(false)
            }

        })


        viewModel.editUserName.observe(this, Observer {
            if (ed_name.text.toString() != it) ed_name.setText(it)
        })

        viewModel.editBio.observe(this, Observer {
            if (ed_bio.text.toString() != it) ed_bio.setText(it)
        })

        viewModel.launchProfileFragment.observe(this, Observer {


            it.getIfNotHandled().run {

                if (ed_name.text.toString().isNotEmpty()) {
                    ed_name.text?.clear()
                }

                if (ed_bio.text.toString().isNotEmpty()) {
                    ed_bio.text?.clear()
                }


            }
            showMessage(getString(R.string.user_info_updated))

        })


    }

    private fun showProgressDialog(progressStatus: Boolean) {
        if (progressStatus) {

            mProgressDialog = ProgressDialog(this, R.style.MyAlertDialogStyle)
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
        } else {
            if (mProgressDialog.isShowing) {
                mProgressDialog.dismiss()
            }
        }

    }
}
