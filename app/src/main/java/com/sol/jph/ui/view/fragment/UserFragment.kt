package com.sol.jph.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sol.jph.App
import com.sol.jph.databinding.FragmentUserBinding
import com.sol.jph.di.factory.ViewModelFactory
import com.sol.jph.ui.adapter.AlbumAdapter
import com.sol.jph.ui.broadcast.ConnectionStateBroadcastReceiver
import com.sol.jph.model.NetworkAvailability
import com.sol.jph.ui.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_comment.*
import javax.inject.Inject

class UserFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: UserViewModel

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var albumAdapter: AlbumAdapter

    private val args: UserFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        App.instance.appComponent.inject(this)
        _binding = FragmentUserBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)

        setupRecyclerView()

        handleConnectionState()

        getUserId()

        observe()

        getUser()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        val rView: RecyclerView = binding.albumView
        val layoutManager = GridLayoutManager(activity, GridLayoutManager.VERTICAL)
        rView.layoutManager = layoutManager

        albumAdapter =
            AlbumAdapter(
                listOf())
        rView.adapter = albumAdapter

        rView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastPosition: Int = layoutManager.findLastVisibleItemPosition()
                val listSize: Int = viewModel.albums.value!!.size

                if (!viewModel.isLoading.value!! && listSize == (lastPosition + 1)) {
                    viewModel.getUserAlbum()
                }
            }
        })
    }

    private fun observe(){
        viewModel.user.observe(viewLifecycleOwner, {
            binding.user = it!!
            println("user name ${it.username} name ${it.name} email ${it.email}")
        })

        viewModel.albums.observe(viewLifecycleOwner, {
            albumAdapter.update(it!!)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, {
            if(it)
                ProgressBar.visibility = View.VISIBLE
            else
                ProgressBar.visibility = View.GONE
        })
    }

    private fun getUserId(){
        viewModel.userId = args.userId
        println(viewModel.userId)
    }

    private fun getUser(){
        viewModel.getUser()
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