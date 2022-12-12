package com.example.secure_unico

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.secure_unico.databinding.FragmentLoginBinding
import com.example.secure_unico.model.SealedApiStatus
import com.example.secure_unico.model.UserViewModel
import kotlinx.coroutines.launch




class LoginFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentLoginBinding




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val fragmentBinding = FragmentLoginBinding
            .inflate(inflater, container, false)
        binding = fragmentBinding
        lifecycleScope.launch{
            userViewModel.loginStatus.collect{
                when(userViewModel.loginStatus.value) {
                    SealedApiStatus.ERROR ->  Toast.makeText(context, "Wrong username/password", Toast.LENGTH_SHORT).show()

                    SealedApiStatus.DONE -> {
                        findNavController().navigate(R.id.action_loginFragment_to_listFragment)
                        userViewModel.restoreStatus()
                    }
                    else -> {}
                }
            }
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = userViewModel
            lifecycleOwner = viewLifecycleOwner
            loginFragment = this@LoginFragment
            loginButton.setOnClickListener { login() }
        }

    }

    override fun onStop() {
        super.onStop()
        binding.usernameText.text?.replace(0, (binding.usernameText.text?.length ?: 1), "")
        binding.passwordText.text?.replace(0, (binding.passwordText.text?.length ?: 1), "")
    }


    fun login() {
        binding.usernameTextField.error = null
        binding.passwordTextField.error = null
        var isOneOfTheFieldsEmpty = false
        if (binding.usernameText.text.isNullOrBlank()) {
            binding.usernameTextField.error = "The field is blank"
            isOneOfTheFieldsEmpty = true
        }
        if (binding.passwordText.text.isNullOrBlank()) {
            binding.passwordTextField.error = "The field is blank"
            isOneOfTheFieldsEmpty = true
        }
        if (!isOneOfTheFieldsEmpty) {
            //richiesta di login

            userViewModel.getToken(
                binding.usernameText.text.toString(),
                binding.passwordText.text.toString()
            )

/*
            if (userViewModel.loginStatus.value == SealedApiStatus.ERROR) {
                Toast.makeText(context, "Wrong username/password", Toast.LENGTH_SHORT).show()
            } else if(userViewModel.loginStatus.value == SealedApiStatus.DONE) {
                findNavController().navigate(R.id.action_loginFragment_to_listFragment)
            }

 */










        }

    }




}