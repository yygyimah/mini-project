package com.gyimah.lavori.viewmodels

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.gyimah.lavori.listeners.PostListener
import com.gyimah.lavori.models.Post
import com.gyimah.lavori.repositories.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
): ViewModel(), PostListener {

    init {
        postRepository.setPostListener(this)
    }

    val postState = MutableLiveData<List<Post>>()
    val errorMessageState = MutableLiveData<String>()
    val addPostState = MutableLiveData<Int>()


    fun getPosts() {
        postRepository.getPosts()
    }

    fun storePost(content: String, fileUri: Uri?) {
        when {
            content.trim().isEmpty() -> {

                errorMessageState.postValue("Post content is required")

            }else -> {

                postRepository.storePost(content, fileUri)
            }
        }

    }

    override fun onPostsRetrieved(posts: MutableList<Post>) {
        postState.postValue(posts)
    }

    override fun onPostsError(message: String) {
        errorMessageState.postValue(message)
    }

    override fun onAddPostSuccess() {
        addPostState.postValue(1);
    }

    override fun onAddPostFailure(message: String) {
        addPostState.postValue(0)
        errorMessageState.postValue(message)
    }
}