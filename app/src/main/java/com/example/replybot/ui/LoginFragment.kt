package com.example.replybot.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.replybot.R
import com.example.replybot.databinding.FragmentLoginBinding
import com.example.replybot.viewModels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val viewModel: LoginViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_login

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)

        binding?.btnLogin?.setOnClickListener {
            val email = binding?.email?.text.toString()
            val password = binding?.password?.text.toString()
            viewModel.login(email,password)
        }

        binding?.goRegister?.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            NavHostFragment.findNavController(this).navigate(action)
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.loginFinish.collect {
                val action = LoginFragmentDirections.actionLoginFragmentToRulesFragment()
                navController.navigate(action)

            }
        }
    }
}