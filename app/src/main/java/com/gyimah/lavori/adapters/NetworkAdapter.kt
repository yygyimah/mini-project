package com.gyimah.lavori.adapters

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.gyimah.lavori.R
import com.gyimah.lavori.models.Post
import com.gyimah.lavori.models.User
import com.squareup.picasso.Picasso
import javax.inject.Inject

class NetworkAdapter @Inject constructor(val application: Application) : Adapter<NetworkAdapter.NetworkViewHolder>() {

    private val users: MutableList<User> = mutableListOf();

    fun setUsers(users: List<User>) {
        this.users.clear()
        this.users.addAll(users)
    }

    fun getUsers() = this.users


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetworkViewHolder {
        return NetworkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.network_item_row, parent, false))
    }

    override fun onBindViewHolder(holder: NetworkViewHolder, position: Int) {
        val user = users[position]

        holder.name.text = application.getString(R.string.full_name, user.firstName, user.lastName)
        holder.headline.text = user.summary

        if (user.imageUrl != null && user.imageUrl != "") {
            Picasso.get().load(user.imageUrl).placeholder(R.drawable.person).into(holder.image)
        }

    }

    override fun getItemCount(): Int {
        return users.size
    }

    class NetworkViewHolder(itemView: View) : ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.name)
        val image: ImageView = itemView.findViewById(R.id.profile_image)
        val headline: TextView = itemView.findViewById(R.id.headline)
    }
}