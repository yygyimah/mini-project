package com.gyimah.lavori.adapters

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.gyimah.lavori.R
import com.gyimah.lavori.listeners.ItemClickListener
import com.gyimah.lavori.listeners.PostLikeShareListener
import com.gyimah.lavori.models.Post
import com.squareup.picasso.Picasso
import javax.inject.Inject

class PostAdapter @Inject constructor(private val application: Application) : Adapter<PostAdapter.PostViewHolder>() {

    private lateinit var listener: ItemClickListener

    private lateinit var postLikeShareListener: PostLikeShareListener

    private val posts: MutableList<Post> = mutableListOf();

    fun setPosts(posts: List<Post>) {
        this.posts.clear()
        this.posts.addAll(posts)
    }

    fun getPosts() = this.posts


    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    fun setPostLikeShareListener(postLikeShareListener: PostLikeShareListener) {
        this.postLikeShareListener = postLikeShareListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post_item_row, parent, false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        val post = posts[position]

        val user = post.user!!


        if (post.imageUrl == null) {
            holder.image.visibility = View.GONE

        }else {

            Picasso.get().load(post.imageUrl).placeholder(R.drawable.placeholder).into(holder.image)
            holder.image.visibility = View.VISIBLE
        }

        if (user.imageUrl != null && user.imageUrl != "") {
            Picasso.get().load(user.imageUrl).into(holder.posterImage)
        }

        holder.posterName.text = application.getString(R.string.post_name, user.firstName, user.lastName)

        holder.content.text = application.getString(R.string.post_content_summary, post.content)

        holder.posterHeadline.text = user.summary

        holder.itemView.setOnClickListener {
            listener.onItemClicked(it, position)
        }

        holder.like.setOnClickListener {
            postLikeShareListener.onLikedClicked(position)
        }

        holder.share.setOnClickListener {
            postLikeShareListener.onShareClicked(position)
        }



    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class PostViewHolder(itemView: View) : ViewHolder(itemView) {

        val posterImage: ImageView = itemView.findViewById(R.id.poster_image)
        val posterName: TextView = itemView.findViewById(R.id.poster_name)
        val posterHeadline: TextView = itemView.findViewById(R.id.poster_headline)
        val content: TextView = itemView.findViewById(R.id.content)
        val image: ImageView = itemView.findViewById(R.id.image)
        val like: RelativeLayout = itemView.findViewById(R.id.like_layout)
        val comment: RelativeLayout = itemView.findViewById(R.id.comment_layout)
        val share: RelativeLayout = itemView.findViewById(R.id.share_layout)



    }
}