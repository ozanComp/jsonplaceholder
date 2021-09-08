package com.sol.jph.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sol.jph.databinding.CommentItemBinding
import com.sol.jph.model.Comment

class CommentAdapter (var comments: List<Comment>)
    : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(val binding: CommentItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun databind(comment: Comment){
            binding.comment = comment
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CommentItemBinding.inflate(layoutInflater)

        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int)
            = holder.databind(comments[position])

    override fun getItemCount(): Int {
        return comments.size
    }

    fun updateComments(comments: List<Comment>){
        this.comments = comments
        notifyDataSetChanged()
    }
}