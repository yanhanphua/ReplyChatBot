package com.example.replybot.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.replybot.R
import com.example.replybot.adapter.RulesAdapter
import com.example.replybot.databinding.FragmentActivatedRulesBinding
import com.example.replybot.databinding.FragmentDeactivatedRulesBinding
import com.example.replybot.service.AuthService
import com.example.replybot.viewModels.ActivatedRulesViewModel
import com.example.replybot.viewModels.DeactivatedRulesViewModel
import com.example.replybot.viewModels.RulesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class DeactivatedRulesFragment : BaseFragment<FragmentDeactivatedRulesBinding>() {
    override val viewModel: DeactivatedRulesViewModel by viewModels()
    private lateinit var adapter: RulesAdapter

    private val parentViewModel: RulesViewModel by viewModels(
        ownerProducer ={
            requireParentFragment()
        }
    )

    @Inject
    lateinit var authService: AuthService

    override fun getLayoutResource() = R.layout.fragment_deactivated_rules

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)

        setupAdapter()
        parentViewModel.refreshDeactivatedRules.observe(viewLifecycleOwner){
            if(it){
                refresh()
                parentViewModel.onRefreshDeactivatedRules(false)
            }
            Log.d("ewqewqewq","from Deactivate $it")
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        viewModel.allRules.observe(viewLifecycleOwner) { it ->
            val activatedRules = it.filter {
                !it.activation
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
                it.copy(activation = true),
                it
            )
            val bundle = Bundle()
            bundle.putBoolean("refresh", true)
            Log.d("ewqewqewq","ewqewqewq")
            parentViewModel.onRefreshActivatedRules(true)
            viewModel.getRules()
        })
        binding?.rvItems?.adapter = adapter
        binding?.rvItems?.layoutManager = layoutManager
    }

    fun refresh() {
        viewModel.getRules()
    }

    companion object {
        private var deactivatedRulesFragmentInstance: DeactivatedRulesFragment? = null
        fun getInstance(): DeactivatedRulesFragment {
            if (deactivatedRulesFragmentInstance == null) {
                deactivatedRulesFragmentInstance = DeactivatedRulesFragment()
            }

            return deactivatedRulesFragmentInstance!!
        }
    }
}