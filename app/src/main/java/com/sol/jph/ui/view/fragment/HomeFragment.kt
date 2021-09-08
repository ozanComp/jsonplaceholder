package com.sol.jph.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sol.jph.App
import com.sol.jph.databinding.FragmentHomeBinding
import com.sol.jph.di.factory.ViewModelFactory
import com.sol.jph.ui.viewmodel.HomeViewModel
import javax.inject.Inject

class HomeFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: HomeViewModel

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        App.instance.appComponent.inject(this)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        observe()

        getLoggedUser()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observe(){
        viewModel.userEntity.observe(viewLifecycleOwner, {
            binding.userEntity = it!!
        })
    }

    private fun getLoggedUser(){
        viewModel.getLoggedUser()
    }
}