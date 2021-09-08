package com.sol.jph.ui.view.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sol.jph.App
import com.sol.jph.databinding.FragmentCommentBinding
import com.sol.jph.di.factory.ViewModelFactory
import com.sol.jph.ui.adapter.CommentAdapter
import com.sol.jph.ui.broadcast.ConnectionStateBroadcastReceiver
import com.sol.jph.model.NetworkAvailability
import com.sol.jph.ui.viewmodel.CommentViewModel
import kotlinx.android.synthetic.main.fragment_comment.*
import javax.inject.Inject

class CommentFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: CommentViewModel

    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding!!
    private lateinit var commentAdapter: CommentAdapter

    private val args: CommentFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        App.instance.appComponent.inject(this)
        _binding = FragmentCommentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, viewModelFactory).get(CommentViewModel::class.java)

        setupRecyclerView()

        handleConnectionState()

        getPostId()

        observe()

        getComment()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        val rView: RecyclerView = binding.commentView
        val layoutManager = GridLayoutManager(activity, GridLayoutManager.VERTICAL)
        rView.layoutManager = layoutManager

        commentAdapter =
            CommentAdapter(
                listOf())
        rView.adapter = commentAdapter

        rView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastPosition: Int = layoutManager.findLastVisibleItemPosition()
                val listSize: Int = viewModel.commentList.value!!.size

                if (!viewModel.isLoading.value!! && listSize == (lastPosition + 1)) {
                    viewModel.getComment()
                }
            }
        })
    }

    private fun observe(){
        viewModel.commentList.observe(viewLifecycleOwner, {
            binding.comment = it!!.first()
            commentAdapter.updateComments(it)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, {
            if(it)
                ProgressBar.visibility = View.VISIBLE
            else
                ProgressBar.visibility = View.GONE
        })
    }

    private fun getPostId(){
        viewModel.postId = args.postId
        println(viewModel.postId)
    }

    private fun getComment(){
        viewModel.getComment()
    }

    private fun handleConnectionState(){
        ConnectionStateBroadcastReceiver.get(requireContext()).observe(viewLifecycleOwner, {
            if (it == NetworkAvailability.CONNECTED) {
                println("Connected")
                binding.list.visibility = View.VISIBLE
                binding.warning.visibility = View.GONE
            }else{
                println("Not Connected")
                binding.warning.visibility = View.VISIBLE
                binding.list.visibility = View.GONE
            }
        })
    }
}