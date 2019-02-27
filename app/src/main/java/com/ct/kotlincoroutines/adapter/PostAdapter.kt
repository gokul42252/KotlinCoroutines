package com.ct.kotlincoroutines.adapter

import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ct.kotlincoroutines.R
import com.ct.kotlincoroutines.data.PlaceholderPosts
import com.squareup.picasso.Picasso

class PostAdapter(private val postList: List<PlaceholderPosts>) :
    RecyclerView.Adapter<PostAdapter.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ItemHolder {
        return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val post = postList[position]
        holder.postName.text = post.title
        holder.postPreview.text = post.body

    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postName = itemView.findViewById<AppCompatTextView>(R.id.postName)!!
        val postPreview = itemView.findViewById<AppCompatTextView>(R.id.postPreview)!!


    }
}