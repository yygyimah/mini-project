package com.gyimah.lavori.repositories

import android.app.Application
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.gyimah.lavori.constants.Constants
import com.gyimah.lavori.listeners.PostListener
import com.gyimah.lavori.models.Post
import com.gyimah.lavori.utils.Session
import java.io.File
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    application: Application,
    private val storageReference: StorageReference

){

    private lateinit var postListener: PostListener

    private var session: Session = Session.getInstance(application.applicationContext)

    fun setPostListener(listener: PostListener) {
        this.postListener = listener
    }

    fun getPosts() {
        val posts = mutableListOf<Post>()

        firebaseFirestore.collection(Constants.POSTS)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("POST REPO", e.message!!)
                    postListener.onPostsError("Error fetching posts")
                }

                if (snapshot != null) {

                    posts.clear()

                    for (item in snapshot.documents) {

                        val post = item.toObject<Post>()

                        Log.i("POST", post.toString())

                        if (post != null) {
                            posts.add(post)
                        }

                    }
                }

                postListener.onPostsRetrieved(posts)

            }

    }

    fun storePost(content: String, fileUri: Uri?) {

        val ref = storageReference.child(Constants.POST_IMAGES)

       if (fileUri != null) {

           val uploadTask = ref.putFile(Uri.fromFile(File(fileUri.path.toString())))

           uploadTask.continueWithTask { task: Task<UploadTask.TaskSnapshot?> ->
               if (!task.isSuccessful) {
                    postListener.onAddPostFailure("Error uploading image, please try again later")
               }
               ref.downloadUrl
           }.addOnSuccessListener { task: Uri ->
               savePost(
                   content = content,
                   imageUrl =  task.toString()
               )
           }.addOnFailureListener { e: Exception -> postListener.onAddPostFailure(e.message!!) }


       }else {
           savePost(content = content)
       }
    }

    private fun savePost(content: String, imageUrl: String? = null) {
        val user = session.getUser()
        val key = firebaseFirestore.collection(Constants.POSTS).document().id
        firebaseFirestore.collection(Constants.POSTS)
            .document(key)
            .set(Post(id = key, imageUrl= imageUrl, content = content, user = user))
            .addOnSuccessListener {
                postListener.onAddPostSuccess()
            }
            .addOnFailureListener {
                postListener.onAddPostFailure(it.localizedMessage!!)
            }
    }
}