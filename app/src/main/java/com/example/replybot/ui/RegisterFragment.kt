package com.example.replybot.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.replybot.R
import com.example.replybot.databinding.FragmentRegisterBinding
import com.example.replybot.viewModels.RegisterViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    override val viewModel: RegisterViewModel by viewModels()

    override fun getLayoutResource() = R.layout.fragment_register

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        binding.run{
            binding?.btnRegister?.setOnClickListener {
                val username = binding?.username?.text?.toString()
                val email = binding?.email?.text?.toString()
                val password = binding?.password?.text.toString()
                val conPass = binding?.confirmPass?.text.toString()
                val id=UUID.randomUUID().toString()
                if(username?.length!! < 2 && password.length <6){
                    val snackBar =
                        Snackbar.make(
                            binding!!.root,"Please enter all the values",Snackbar.LENGTH_LONG
                        )
                    snackBar.show()
                }else{
                    lifecycleScope.launch {
                        if (email != null) {
                            viewModel.register(id,username,email,password,conPass)
                        }
                    }
                }
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.finish.collect {
                val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                navController.navigate(action)
            }
        }
    }

}