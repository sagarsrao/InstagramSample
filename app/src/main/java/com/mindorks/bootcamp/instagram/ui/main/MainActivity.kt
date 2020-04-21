package com.mindorks.bootcamp.instagram.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
import com.mindorks.bootcamp.instagram.ui.home.HomeFragment
import com.mindorks.bootcamp.instagram.ui.photo.PhotoFragment
import com.mindorks.bootcamp.instagram.ui.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel>() {

    companion object {
        const val TAG = "MainActivity"
    }

    var activeFragment: Fragment? = null

    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel

    override fun provideLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }


    override fun setupView(savedInstanceState: Bundle?) {

        bottomNavigation.run {
            itemIconTintList = null
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.action_home -> {
                        viewModel.homeClicked()
                        true
                    }
                    R.id.action_photo -> {
                        viewModel.photosClicked()
                        true
                    }
                    R.id.action_profile -> {
                        viewModel.profileClicked()
                        true
                    }
                    else -> false
                }
            }
        }


    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.switchToHome.observe(this, Observer {
            it.getIfNotHandled().run {

                showHome()
            }
        })

        viewModel.switchToPhotos.observe(this, Observer {
            it?.getIfNotHandled().run {

                showAddPhoto()
            }
        })


        viewModel.switchToProfile.observe(this, Observer {
            it?.getIfNotHandled().run {

                showProfile()
            }
        })

        mainSharedViewModel.homeRedirection.observe(this, Observer {

            it?.getIfNotHandled().run {

                bottomNavigation?.selectedItemId = R.id.rv_home
            }
        })


    }

    private fun showHome() {
        if (activeFragment is HomeFragment) return

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        var fragment = supportFragmentManager.findFragmentByTag(HomeFragment.TAG) as HomeFragment?

        if (fragment == null) {
            fragment = HomeFragment.newInstance("", "")
            fragmentTransaction.add(R.id.container_fragment_main, fragment, HomeFragment.TAG)
        } else {
            fragmentTransaction.show(fragment)
        }

        if (activeFragment != null) fragmentTransaction.hide(activeFragment as Fragment)

        fragmentTransaction.commit()

        activeFragment = fragment
    }

    private fun showProfile() {
        if (activeFragment is ProfileFragment) return

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        var fragment =
            supportFragmentManager.findFragmentByTag(ProfileFragment.TAG) as ProfileFragment?

        if (fragment == null) {
            fragment = ProfileFragment.newInstance("", "")
            fragmentTransaction.add(R.id.container_fragment_main, fragment, ProfileFragment.TAG)
        } else {
            fragmentTransaction.show(fragment)
        }

        if (activeFragment != null) fragmentTransaction.hide(activeFragment as Fragment)

        fragmentTransaction.commit()

        activeFragment = fragment
    }

    private fun showAddPhoto() {
        if (activeFragment is PhotoFragment) return

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        var fragment = supportFragmentManager.findFragmentByTag(PhotoFragment.TAG) as PhotoFragment?

        if (fragment == null) {
            fragment = PhotoFragment.newInstance("", "")
            fragmentTransaction.add(R.id.container_fragment_main, fragment, PhotoFragment.TAG)
        } else {
            fragmentTransaction.show(fragment)
        }

        if (activeFragment != null) fragmentTransaction.hide(activeFragment as Fragment)

        fragmentTransaction.commit()

        activeFragment = fragment
    }


}
