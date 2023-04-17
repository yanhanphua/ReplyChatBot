package com.example.replybot.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.replybot.R
import com.example.replybot.adapter.RulesAdapter
import com.example.replybot.databinding.FragmentActivatedRulesBinding
import com.example.replybot.service.AuthService
import com.example.replybot.viewModels.ActivatedRulesViewModel
import com.example.replybot.viewModels.RulesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ActivatedRulesFragment : BaseFragment<FragmentActivatedRulesBinding>() {

    override val viewModel: ActivatedRulesViewModel by viewModels()
    private lateinit var adapter: RulesAdapter
    @Inject
    lateinit var authService: AuthService

    private val parentViewModel:RulesViewModel by viewModels(
        ownerProducer ={
            requireParentFragment()
        }
    )

    override fun getLayoutResource() = R.layout.fragment_activated_rules

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)

        setupAdapter()

        binding?.let {
            it.btnAdd.setOnClickListener {
                val action = RulesFragmentDirections.actionRulesFragmentToAddRulesFragment()
                navController.navigate(action)
            }
        }
        parentViewModel.refreshActivatedRules.observe(viewLifecycleOwner){
            if(it){
                refresh()
                parentViewModel.onRefreshActivatedRules(false)
            }
            Log.d("ewqewqewq","from activated $it")
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        viewModel.allRules.observe(viewLifecycleOwner) {
            val activatedRules = it.filter {
                it.activation
            }
            adapter.setRules(activatedRules)
        }
    }

    private fun setupAdapter() {
        val layoutManager = LinearLayoutManager(requireContext())
        adapter = RulesAdapter(emptyList(), {
            viewModel.removeRules(authService.getUserUid().toString(), it)
            viewModel.getRules()
        }, {
            val action = RulesFragmentDirections.actionRulesFragmentToEditRulesFragment(it.id)
            navController.navigate(action)
        }, {
            viewModel.activationRules(
                authService.getUserUid().toString(),
                it.copy(activation = false),
                it
            )
            val bundle = Bundle()
            bundle.putBoolean("refresh", true)
            parentViewModel.onRefreshDeactivatedRules(true)
            viewModel.getRules()
        })
        binding?.rvItems?.adapter = adapter
        binding?.rvItems?.layoutManager = layoutManager
    }

    private fun refresh() {
        viewModel.getRules()
    }

    companion object {
        private var activatedRulesFragmentInstance: ActivatedRulesFragment? = null
        fun getInstance(): ActivatedRulesFragment {
            if (activatedRulesFragmentInstance == null) {
                activatedRulesFragmentInstance = ActivatedRulesFragment()
            }

            return activatedRulesFragmentInstance!!
        }
    }
}