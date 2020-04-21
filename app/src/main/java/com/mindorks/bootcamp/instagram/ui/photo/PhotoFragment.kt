package com.mindorks.bootcamp.instagram.ui.photo

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.model.allPost.DataItem
import com.mindorks.bootcamp.instagram.di.component.FragmentComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment
import com.mindorks.bootcamp.instagram.ui.editProfile.EditProfileActivity
import com.mindorks.bootcamp.instagram.ui.main.MainActivity
import com.mindorks.bootcamp.instagram.ui.main.MainSharedViewModel
import com.mindorks.bootcamp.instagram.utils.common.Event
import com.mindorks.bootcamp.instagram.utils.common.IImageCompressTaskListener
import com.mindorks.bootcamp.instagram.utils.common.ImageCompressTask
import com.mindorks.bootcamp.instagram.utils.common.SelectedFilePath
import kotlinx.android.synthetic.main.fragment_photo.*
import java.io.File
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PhotoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhotoFragment : BaseFragment<PhotoViewModel>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var mProgressDialog: ProgressDialog

    var executorService: ExecutorService = Executors.newFixedThreadPool(1)

    var imageCompressTask: ImageCompressTask? = null
    var mCurrentPhotoPath: String? = null

    var userData: User? = null

    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PhotoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        const val TAG = "PhotoFragment"
        const val GALLERY = 1
        const val CAMERA = 2
    }

    override fun provideLayoutId(): Int {
        return R.layout.fragment_photo;
    }


    override fun injectDependencies(fragmentComponent: FragmentComponent) {

        fragmentComponent.inject(this)

    }

    override fun setupView(view: View) {


        iv_camera.setOnClickListener {

            validatePermissions(CAMERA)
        }


        iv_gallery.setOnClickListener {
            validatePermissions(GALLERY)
        }
    }


    override fun setupObservers() {
        super.setupObservers()


        viewModel.savingData.observe(this, androidx.lifecycle.Observer {
            if (it) {
                showProgressDialog(true)
            } else {
                showProgressDialog(false)
            }

        })

        viewModel.successImageAttached.observe(this, Observer {
            /*CreatePost after getting Image URL from the image attached*/
            viewModel.doCreatePost(it)

        })

        viewModel.passUserData.observe(this, Observer { userInfo ->

            userData = userInfo

        })
        viewModel.createPostSuccess.observe(this, Observer {

            Toast.makeText(activity!!, "Create Post Success", Toast.LENGTH_LONG).show()


            mainSharedViewModel.onHomeRedirect()
            mainSharedViewModel.newPost.postValue(
                (Event(
                    DataItem(
                        it.imgUrl, it.createdAt!!, it.imgWidth,
                        arrayListOf(), it.imgHeight, it.id,
                        user = DataItem.LikedByItem(
                            userData?.profilePicUrl,
                            userData?.name,
                            userData?.id
                        )
                    )
                ))
            )

        })

        viewModel.launchMainActivity.observe(this, Observer {

            activity?.startActivity(Intent(activity!!,MainActivity::class.java))
        })


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == EditProfileActivity.GALLERY) {
            if (data != null) {
                try {
                    Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show()
                    val path = SelectedFilePath.getPath(activity, data.data)
                    viewModel.doUploadImage(path)


                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(activity, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        if (requestCode == CAMERA && resultCode == RESULT_OK) {
            try {
                processCapturedPhoto()
            } catch (e: Exception) {
            }


        }

    }

    private fun processCapturedPhoto() {
        val cursor = activity?.contentResolver?.query(
            Uri.parse(mCurrentPhotoPath),
            Array(1) { android.provider.MediaStore.Images.ImageColumns.DATA },
            null, null, null
        )
        cursor?.moveToFirst()
        val photoPath = cursor?.getString(0)
        cursor?.close()
        val file = File(photoPath)
        Log.d("Original File Size", "" + file.length())
        val uri = Uri.fromFile(file)
        imageCompressTask = ImageCompressTask(activity, file.path, iImageCompressTaskListener)
        executorService.execute(imageCompressTask)


    }

    private fun validatePermissions(code: Int) {
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(
                    response: PermissionGrantedResponse?
                ) {

                    iv_camera.setOnClickListener {
                        launchCamera()

                    }
                    if (code == GALLERY) {
                        iv_gallery.setOnClickListener {
                            val galleryIntent =
                                Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                )
                            startActivityForResult(galleryIntent, GALLERY)
                        }
                    }


                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    AlertDialog.Builder(activity!!)
                        .setMessage(
                            "For adding images , You need to provide permission to access your files"
                        )
                        .setNegativeButton(
                            android.R.string.cancel
                        ) { dialog, _ ->
                            dialog.dismiss()
                            token?.cancelPermissionRequest()
                        }
                        .setPositiveButton(
                            android.R.string.ok
                        ) { dialog, _ ->
                            dialog.dismiss()
                            token?.continuePermissionRequest()
                        }
                        .setOnDismissListener {
                            token?.cancelPermissionRequest()
                        }
                        .show()
                }

                override fun onPermissionDenied(
                    response: PermissionDeniedResponse?
                ) {

                    val snack = Snackbar.make(
                        container_photo_fragment,
                        R.string.storage_permission_denied_message,
                        Snackbar.LENGTH_LONG
                    )
                    snack.setActionTextColor(Color.parseColor("#BB4444"))
                    snack.view.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    val textView = snack.view.findViewById(R.id.snackbar_text) as TextView
                    textView.setTextColor(Color.parseColor("#4444DD"))
                    snack.show()
                }
            })
            .check()
    }


    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        val fileUri = activity?.contentResolver
            ?.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(activity?.packageManager) != null) {
            mCurrentPhotoPath = fileUri.toString()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            startActivityForResult(intent, CAMERA)
        }
    }

    private fun showProgressDialog(progressStatus: Boolean) {
        if (progressStatus) {
            mProgressDialog = ProgressDialog(activity, R.style.MyAlertDialogStyle)
            mProgressDialog.setMessage(getString(R.string.progress_upload_message))
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


    private val iImageCompressTaskListener: IImageCompressTaskListener =
        object : IImageCompressTaskListener {
            override fun onComplete(compressed: List<File>) {
                //photo compressed. Yay!

                //prepare for uploads. Use an Http library like Retrofit, Volley or async-http-client (My favourite)
                val file = compressed[0]
                Log.d(
                    "ImageCompressor",
                    "New photo size ==> " + file.length()
                ) //log new file size.
                val path = SelectedFilePath.getPath(activity, Uri.fromFile(file))
                viewModel.doUploadImage(path)
            }

            override fun onError(error: Throwable) {
                //very unlikely, but it might happen on a device with extremely low storage.
                //log it, log.WhatTheFuck?, or show a dialog asking the user to delete some files....etc, etc
                Log.wtf("ImageCompressor", "Error occurred", error)
            }
        }


}
