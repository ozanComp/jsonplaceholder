package com.sol.jph.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.sol.jph.R
import com.sol.jph.databinding.PhotoItemBinding
import com.sol.jph.model.Photo

class PhotoAdapter (var photos: List<Photo>)
    : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(val binding: PhotoItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun databind(photo: Photo){
            binding.photo = photo

            val image = itemView.findViewById<ImageView>(R.id.photo_img)
            Glide
                .with(itemView.context)
                .load(photo.thumbnailUrl + ".png")
                .centerCrop()
                .placeholder(R.drawable.ic_user_24dp)
                .priority(Priority.IMMEDIATE)
                .into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PhotoItemBinding.inflate(layoutInflater)

        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int)
            = holder.databind(photos[position])

    override fun getItemCount(): Int {
        return photos.size
    }

    fun update(photos: List<Photo>){
        this.photos = photos
        notifyDataSetChanged()
    }
}