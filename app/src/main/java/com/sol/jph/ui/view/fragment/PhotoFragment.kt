package com.sol.jph.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sol.jph.App
import com.sol.jph.databinding.FragmentPhotoBinding
import com.sol.jph.di.factory.ViewModelFactory
import com.sol.jph.ui.adapter.PhotoAdapter
import com.sol.jph.ui.broadcast.ConnectionStateBroadcastReceiver
import com.sol.jph.model.NetworkAvailability
import com.sol.jph.ui.viewmodel.PhotoViewModel
import kotlinx.android.synthetic.main.fragment_comment.*
import javax.inject.Inject

class PhotoFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: PhotoViewModel

    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!
    private lateinit var photoAdapter: PhotoAdapter

    private val args: PhotoFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        App.instance.appComponent.inject(this)
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, viewModelFactory).get(PhotoViewModel::class.java)

        setupRecyclerView()

        handleConnectionState()

        getAlbumId()

        getPhoto()

        observe()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Glide.get(requireContext()).clearMemory()
    }

    private fun setupRecyclerView() {
        val rView: RecyclerView = binding.photoView
        val layoutManager = GridLayoutManager(activity, 2)
        rView.layoutManager = layoutManager

        photoAdapter =
            PhotoAdapter(
                listOf())
        rView.adapter = photoAdapter

        rView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastPosition: Int = layoutManager.findLastVisibleItemPosition()
                val listSize: Int = viewModel.photos.value!!.size

                if (!viewModel.isLoading.value!! && listSize == (lastPosition + 1)) {
                    viewModel.getPhoto()
                }
            }
        })
    }

    private fun observe(){
        viewModel.photos.observe(viewLifecycleOwner, {
            photoAdapter.update(it!!)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, {
            if(it)
                ProgressBar.visibility = View.VISIBLE
            else
                ProgressBar.visibility = View.GONE
        })
    }

    private fun getAlbumId(){
        viewModel.albumId = args.albumId
        println(viewModel.albumId)
    }

    private fun getPhoto(){
        viewModel.getPhoto()
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