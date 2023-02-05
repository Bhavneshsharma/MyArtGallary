package com.bhavneshsharma.myartgallary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bhavneshsharma.myartgallary.R
import com.bhavneshsharma.myartgallary.roomdb.Art
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class ArtRecyclerAdapter @Inject constructor(
    val glide : RequestManager
) : RecyclerView.Adapter<ArtRecyclerAdapter.ArtViewHolder>(){

    class  ArtViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    private val diffUtil = object : DiffUtil.ItemCallback<Art>(){
        override fun areItemsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var arts: List<Art>
    get() = recyclerListDiffer.currentList
    set(value) = recyclerListDiffer.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.art_row,parent,false)
        return ArtViewHolder(view)
    }

    override fun getItemCount(): Int {
       return arts.size
    }

    override fun onBindViewHolder(holder: ArtViewHolder, position: Int) {
    val imageView = holder.itemView.findViewById<ImageView>(R.id.art_row_imageView)
        val nameText = holder.itemView.findViewById<TextView>(R.id.art_row_imageName)
        val artistNameText = holder.itemView.findViewById<TextView>(R.id.art_row_artistName)
        val artYear = holder.itemView.findViewById<TextView>(R.id.art_row_artYear)
        val art = arts[position]
        holder.itemView.apply {
            nameText.text = "Name : ${art.name}"
            artistNameText.text = "Artist Name: ${art.artistName}"
            artYear.text = "Year: ${art.year}"
            glide.load(art.imageUrl).into(imageView)
        }
    }
}