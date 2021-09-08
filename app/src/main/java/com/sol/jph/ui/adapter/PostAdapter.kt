package com.sol.jph.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.sol.jph.databinding.PostItemBinding
import com.sol.jph.model.PostInfo
import com.sol.jph.ui.view.fragment.PostFragmentDirections
import kotlinx.android.synthetic.main.post_item.view.*

@Suppress("DEPRECATION")
class PostAdapter(var posts: List<PostInfo>)
    : RecyclerView.Adapter<PostAdapter.PostsViewHolder>() {

    inner class PostsViewHolder(val binding: PostItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun databind(postInfo: PostInfo){
            binding.postInfo = postInfo
        }

        init{
            itemView.username.setOnClickListener {
                val action = PostFragmentDirections.actionPostFragmentToUserFragment(posts[position].user!!.id)
                it.findNavController().navigate(action)
            }

            itemView.comments.setOnClickListener {
                val action = PostFragmentDirections.actionPostFragmentToCommentFragment(posts[position].post!!.id)
                it.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PostItemBinding.inflate(layoutInflater)

        return PostsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int)
            = holder.databind(posts[position])

    override fun getItemCount(): Int {
        return posts.size
    }

    fun updatePosts(posts: List<PostInfo>){
        this.posts = posts
        notifyDataSetChanged()
    }
}