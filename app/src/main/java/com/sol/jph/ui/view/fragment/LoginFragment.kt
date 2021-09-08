package com.sol.jph.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.sol.jph.App
import com.sol.jph.databinding.FragmentLoginBinding
import com.sol.jph.di.factory.ViewModelFactory
import com.sol.jph.ui.viewmodel.LoginViewModel
import com.sol.jph.ui.view.activity.DetailActivity
import com.sol.jph.ui.view.activity.SplashActivity
import javax.inject.Inject

class LoginFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: LoginViewModel

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        App.instance.appComponent.inject(this)
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        binding.loginViewModel = viewModel
        binding.lifecycleOwner = this

        finishApp()

        observe()

        //toastService()

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

        viewModel.errorToastEmail .observe(viewLifecycleOwner, { hasError->
            if(hasError==true){
                Toast.makeText(requireContext(), "Please check your e mail", Toast.LENGTH_SHORT).show()
                viewModel.doneToastErrorEmail()
            }
        })

        viewModel.errorToastInvalidPassword.observe(viewLifecycleOwner, { hasError->
            if(hasError==true){
                Toast.makeText(requireContext(), "Please check your password", Toast.LENGTH_SHORT).show()
                viewModel.doneToastInvalidPassword()
            }
        })

        viewModel.errorToastUser .observe(viewLifecycleOwner, { hasError->
            if(hasError==true){
                Toast.makeText(requireContext(), "User does not exist, please sign in", Toast.LENGTH_SHORT).show()
                viewModel.doneToastErrorUser()
            }
        })


        viewModel.navigateToSignIn.observe(viewLifecycleOwner, { hasFinished->
            if (hasFinished == true){
                NavHostFragment.findNavController(this).navigate(LoginFragmentDirections.actionLoginFragmentToSignInFragment())
                viewModel.doneNavigatingSignIn()
            }
        })

        viewModel.navigateToLoggedIn.observe(viewLifecycleOwner, { hasFinished->
            if (hasFinished == true){
                Toast.makeText(requireContext(), "Logged In", Toast.LENGTH_SHORT).show()
                viewModel.doneNavigatingLoggedIn()

                val intent = Intent(requireContext(), DetailActivity::class.java)
                startActivity(intent)
            }
        })
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
}