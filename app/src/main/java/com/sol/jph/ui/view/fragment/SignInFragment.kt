package com.sol.jph.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sol.jph.App
import com.sol.jph.databinding.FragmentSignInBinding
import com.sol.jph.di.factory.ViewModelFactory
import com.sol.jph.ui.view.activity.DetailActivity
import com.sol.jph.ui.viewmodel.SignInViewModel
import javax.inject.Inject

class SignInFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: SignInViewModel

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        App.instance.appComponent.inject(this)
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, viewModelFactory).get(SignInViewModel::class.java)

        binding.signinViewModel = viewModel
        binding.lifecycleOwner = this

        observe()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observe(){
        viewModel.errorToast.observe(viewLifecycleOwner, { hasError->
            if(hasError==true){
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                viewModel.doneToast()
            }
        })

        viewModel.errorToastEmail.observe(viewLifecycleOwner, { hasError->
            if(hasError==true){
                Toast.makeText(requireContext(), "Please check your e mail", Toast.LENGTH_SHORT).show()
                viewModel.doneToastEmail()
            }
        })

        viewModel.errorToastUser.observe(viewLifecycleOwner, { hasError->
            if(hasError==true){
                Toast.makeText(requireContext(), "User was already taken", Toast.LENGTH_SHORT).show()
                viewModel.doneToastErrorUser()
            }
        })

        viewModel.navigateTo.observe(viewLifecycleOwner, { hasFinished->
            if (hasFinished == true){
                Toast.makeText(requireContext(), "Sign in", Toast.LENGTH_SHORT).show()
                viewModel.doneNavigating()

                val intent = Intent(requireContext(), DetailActivity::class.java)
                startActivity(intent)
            }
        })
    }
}