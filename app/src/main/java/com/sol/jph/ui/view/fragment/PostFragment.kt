package com.sol.jph.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sol.jph.App
import com.sol.jph.databinding.FragmentPostBinding
import com.sol.jph.di.factory.ViewModelFactory
import com.sol.jph.model.NetworkAvailability
import com.sol.jph.ui.adapter.PostAdapter
import com.sol.jph.ui.broadcast.ConnectionStateBroadcastReceiver
import com.sol.jph.ui.view.activity.SplashActivity
import com.sol.jph.ui.viewmodel.PostViewModel
import kotlinx.android.synthetic.main.fragment_post.*
import javax.inject.Inject

class PostFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: PostViewModel

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        App.instance.appComponent.inject(this)
        _binding = FragmentPostBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, viewModelFactory).get(PostViewModel::class.java)

        setupRecyclerView()

        handleConnectionState()

        finishApp()

        observe()

        getPosts()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        val rView: RecyclerView = binding.postsView
        val layoutManager = GridLayoutManager(activity, GridLayoutManager.VERTICAL)
        rView.layoutManager = layoutManager

        postAdapter =
            PostAdapter(
                listOf())
        rView.adapter = postAdapter

        rView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastPosition: Int = layoutManager.findLastVisibleItemPosition()
                val listSize: Int = viewModel.postList.value!!.size

                if (!viewModel.isLoading.value!! && listSize == (lastPosition + 1)) {
                    viewModel.getPosts()
                }
            }
        })
    }

    private fun observe(){
        viewModel.postList.observe(viewLifecycleOwner, {
            binding.postInfo = it.first()
            postAdapter.updatePosts(it)
            ProgressBar.visibility = View.GONE
        })

        viewModel.isLoading.observe(viewLifecycleOwner, {
            if(it)
                ProgressBar.visibility = View.VISIBLE
            else
                ProgressBar.visibility = View.GONE
        })
    }

    private fun getPosts(){
        viewModel.getPosts()
    }

    private fun finishApp(){
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val i = Intent(requireContext(), SplashActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    i.putExtra("EXIT", true)
                    startActivity(i)
                    requireActivity().finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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