package com.mindorks.bootcamp.instagram.ui.editProfile

import android.Manifest
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
import com.mindorks.bootcamp.instagram.utils.common.SelectedFilePath
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.File
import java.io.IOException


class EditProfileActivity : BaseActivity<EditProfileActivityViewModel>() {


    lateinit var mProgressDialog: ProgressDialog

    companion object {

        const val GALLERY = 1
    }

    override fun provideLayoutId(): Int {
        return R.layout.activity_edit_profile
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI: Uri? = data.data
                try {
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    //val path = saveImage(bitmap)
                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
                    ivProfile.setImageBitmap(bitmap)
                    val path = SelectedFilePath.getPath(this, data.data)

                    viewModel.doUploadImage(path)


                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }

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


        tvChangePhoto.setOnClickListener {
            chooseImagesFromGallery()
        }

    }

    private fun chooseImagesFromGallery() {

        permission_check(GALLERY)


    }

    override fun goBack() {
        super.goBack()
    }

    override fun setupObservers() {
        super.setupObservers()
        /*Observe all the data coming from ViewModel and do update the activity */

        viewModel.emailInfoSuccess.observe(this, Observer {
            if (it.isNotEmpty()) {
                tvEmailAddress.requestFocus()
                tvEmailInfo.setText(it)
            }
        })

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

        viewModel.successImageAttached.observe(this, Observer {

            viewModel.onImageUrlSave(it)
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

    fun permission_check(code: Int) {
        val hasWriteContactsPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                showMessageOKCancel("For adding images , You need to provide permission to access your files",
                    DialogInterface.OnClickListener { dialog, which ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            code
                        )
                    })
                return
            }
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                code
            )
            return
        }

        if (code == GALLERY) {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY)
        }
    }


    private fun showMessageOKCancel(
        message: String,
        okListener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

}
