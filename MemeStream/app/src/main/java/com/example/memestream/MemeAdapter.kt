package com.example.memestream

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MemeAdapter : RecyclerView.Adapter<MemeAdapter.MemeViewHolder>() {

    private var memes: List<GiphyGif> = emptyList()

    fun setMemes(memes: List<GiphyGif>) {
        this.memes = memes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_meme, parent, false)
        return MemeViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemeViewHolder, position: Int) {
        val meme = memes[position]
        Glide.with(holder.itemView.context)
            .load(meme.images.fixed_height.url)
            .into(holder.memeImage)
    }

    override fun getItemCount(): Int = memes.size

    class MemeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val memeImage: ImageView = itemView.findViewById(R.id.memeImage)
    }
}
