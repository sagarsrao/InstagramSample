package com.mindorks.bootcamp.instagram.ui.home.posts

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.mindorks.bootcamp.instagram.data.model.allPost.DataItem
import com.mindorks.bootcamp.instagram.data.model.allPost.PostListResponse
import com.mindorks.bootcamp.instagram.ui.base.BaseAdapter

class PostsAdapter(
    parentLifecycle: Lifecycle,
    posts: ArrayList<DataItem>
) : BaseAdapter<DataItem, PostsItemViewHolder>(parentLifecycle, posts) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostsItemViewHolder(parent)
}