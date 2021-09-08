package com.sol.jph.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.sol.jph.databinding.AlbumItemBinding
import com.sol.jph.model.Album
import com.sol.jph.ui.view.fragment.UserFragmentDirections

class AlbumAdapter (var albums: List<Album>)
    : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    inner class AlbumViewHolder(val binding: AlbumItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun databind(album: Album){
            binding.album = album
        }

        init{
            itemView.setOnClickListener {
                val action = UserFragmentDirections.actionUserFragmentToPhotoFragment(albums[position].id)
                it.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AlbumItemBinding.inflate(layoutInflater)

        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int)
            = holder.databind(albums[position])

    override fun getItemCount(): Int {
        return albums.size
    }

    fun update(albums: List<Album>){
        this.albums = albums
        notifyDataSetChanged()
    }
}