package com.ct.kotlincoroutines.adapter

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ct.kotlincoroutines.R
import com.ct.kotlincoroutines.data.PlaceholderPhotos
import com.squareup.picasso.Picasso

class PhotoAdapter(mContext: Context, private val postList: List<PlaceholderPhotos>) :
    RecyclerView.Adapter<PhotoAdapter.ItemHolder>() {
    private var context: Context? = null

    init {
        context = mContext
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ItemHolder {
        return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val post = postList[position]
        holder.postName.text = post.title
        holder.postPreview.text = post.title
       Picasso.get().load(post.thumbnailUrl).into(holder.imgView)
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postName = itemView.findViewById<AppCompatTextView>(R.id.postName)!!
        val postPreview = itemView.findViewById<AppCompatTextView>(R.id.postPreview)!!
        val imgView = itemView.findViewById<AppCompatImageView>(R.id.imgView)!!
    }
}