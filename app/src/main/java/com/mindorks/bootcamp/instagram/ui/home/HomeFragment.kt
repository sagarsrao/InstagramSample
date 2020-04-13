package com.mindorks.bootcamp.instagram.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.FragmentComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment
import com.mindorks.bootcamp.instagram.ui.home.posts.PostsAdapter
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : BaseFragment<HomeViewModel>() {


    @Inject
    lateinit var mAdapter: PostsAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        const val TAG = "HomeFragment"
    }

    override fun provideLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun injectDependencies(fragmentComponent: FragmentComponent) {

        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {
        /*Run the recycler view operation to show the data*/
        if (rv_home != null) {
            rv_home!!.apply {
                layoutManager = linearLayoutManager
                adapter = mAdapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        layoutManager?.run {

                            if (this is LinearLayoutManager && itemCount > 0 && itemCount == findLastVisibleItemPosition() + 1) {
                                viewModel.onLoadMore()
                            }
                        }
                    }
                })

            }
        }


    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.posts.observe(this, Observer {

            it.data.run {
                /*Supply the data to the adapter*/
                mAdapter.appendData(this!!)
            }

        })


        viewModel.loading.observe(this, Observer {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

    }

}
