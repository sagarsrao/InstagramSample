package com.mindorks.bootcamp.instagram.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.FragmentComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment
import com.mindorks.bootcamp.instagram.ui.editProfile.EditProfileActivity
import com.mindorks.bootcamp.instagram.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_profile.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : BaseFragment<ProfileViewModel>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        const val TAG = "ProfileFragment"
    }

    override fun provideLayoutId(): Int {
        return R.layout.fragment_profile;
    }

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {

        tvEditProfile.setOnClickListener {
            startActivity(Intent(activity, EditProfileActivity::class.java))

        }

        tvLogOut.setOnClickListener {

            viewModel.doLogOut()
        }
    }


    override fun setupObservers() {
        super.setupObservers()

        viewModel.mProgressInfo.observe(this, Observer {
            if (true) {
                pb_fragment_profile.visibility = View.VISIBLE
            } else {
                pb_fragment_profile.visibility = View.GONE
            }

        })

        viewModel.getInfoComplete.observe(this, Observer {

            if (it.profilePicUrl?.isNotEmpty()!!) {

                Glide.with(activity!!).load(it.profilePicUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_signup)).into(ivProfile)
                pb_fragment_profile.visibility = View.GONE

            }

            if (it.name?.isNotEmpty()!!) {
                tvLoggedInName.text = it.name.toString()
            }

            if (it.tagline?.isNotEmpty()!!) {
                tvTagLine.text = it.tagline.toString()
            }
            if (it.id!!.isNotEmpty()) {
                tvNoOfPosts.text = "2" + " Posts"
            }

        })

        viewModel.launchLoginScreen.observe(this, Observer {
            if(true){
                activity?.startActivity(Intent(activity,LoginActivity::class.java))
                activity?.finish()
            }else{
                showMessage(getString(R.string.network_default_error))
            }
        })

    }
}
