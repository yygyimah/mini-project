package com.gyimah.lavori.listeners

import com.gyimah.lavori.models.Post

interface PostListener {

    fun onPostsRetrieved(posts: MutableList<Post>)

    fun onPostsError(message: String)

    fun onAddPostSuccess();

    fun onAddPostFailure(message: String)
}