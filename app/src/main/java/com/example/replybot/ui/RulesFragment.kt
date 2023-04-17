package com.example.replybot.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.replybot.R
import com.example.replybot.adapter.ViewPagerAdapter
import com.example.replybot.databinding.FragmentRulesBinding
import com.example.replybot.service.AuthService
import com.example.replybot.viewModels.RulesViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RulesFragment : BaseFragment<FragmentRulesBinding>() {
    override val viewModel: RulesViewModel by viewModels()

    @Inject
    lateinit var authService: AuthService
    private val activatedRulesFragment = ActivatedRulesFragment.getInstance()
    private val deactivatedRulesFragment = DeactivatedRulesFragment.getInstance()

    override fun getLayoutResource() = R.layout.fragment_rules

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        val adapter = ViewPagerAdapter(
            listOf(activatedRulesFragment, deactivatedRulesFragment),
            childFragmentManager,
            lifecycle
        )
        binding?.vpRules?.adapter = adapter
        // this is to set the tab layout
        binding?.let {
            TabLayoutMediator(it.tlRules, it.vpRules) { tab, pos ->
                Log.d("eiyo",pos.toString())
                tab.text = when (pos) {
                    0 -> "Activated Rules"
                    else -> "Deactivated Rules"
                }
            }.attach()
        }
        setFragmentResultListener("from_edit"){_,result->
            lifecycleScope.launch {
                delay(1000)
                val refresh = result.getBoolean("refresh")
                if (refresh) {
                    viewModel.onRefreshActivatedRules(true)
                }
            }
        }
        setFragmentResultListener("from_add_item") { _, result ->
            lifecycleScope.launch {
                delay(1000)
                val refresh = result.getBoolean("refresh")
                if (refresh) {
                    Log.d("ewqewqewq", "ewqewqewq")
                    viewModel.onRefreshActivatedRules(true)
                }
            }
        }
//        setFragmentResultListener("from_activated") { _, result ->
//            lifecycleScope.launch {
//                delay(1000)
//                val refresh = result.getBoolean("refresh")
//                if (refresh) {
//                    Log.d("ewqewqewq", "ewqewqewq")
//                    viewModel.onRefreshActivatedRules()
//                    viewModel.onRefreshDeactivatedRules()
//                }
//            }
//        }
//        setFragmentResultListener("from_deactivated") { _, result ->
//            lifecycleScope.launch {
//                val refresh = result.getBoolean("refresh")
//                Log.d("ewqewqewq", "ewqewqewq $refresh")
//                if (refresh) {
//                    viewModel.onRefreshActivatedRules()
//                    viewModel.onRefreshDeactivatedRules()
//                }
//            }
//        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
    }

}