package com.example.replybot.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.example.replybot.R
import com.example.replybot.data.model.Rules
import com.example.replybot.databinding.FragmentEditRulesBinding
import com.example.replybot.databinding.FragmentLoginBinding
import com.example.replybot.service.AuthService
import com.example.replybot.viewModels.EditRulesViewModel
import com.example.replybot.viewModels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditRulesFragment : BaseFragment<FragmentEditRulesBinding>() {
    override val viewModel: EditRulesViewModel by viewModels()

    @Inject
    lateinit var authService: AuthService

    override fun getLayoutResource() = R.layout.fragment_edit_rules

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)

        val navArgs: EditRulesFragmentArgs by navArgs()

        viewModel.getCurrentRule(navArgs.id)
        binding?.let { binding ->
            viewModel.rule.observe(viewLifecycleOwner) { rule ->
                binding.name.setText(rule?.name)
                binding.option1.setText(rule?.firstOption)
                binding.option2.setText(rule?.secondOption)
                binding.reply.setText(rule?.reply)

                binding.btnAdd.setOnClickListener {
                    val name = binding.name.text.toString()
                    val option1 = binding.option1.text.toString()
                    val option2 = binding.option2.text.toString()
                    val reply = binding.reply.text.toString()

                    viewModel.updateRules(
                        authService.getUserUid().toString(),
                        Rules(navArgs.id, name, option1, option2, true, reply),
                        rule
                    )
                    val bundle = Bundle()
                    bundle.putBoolean("refresh", true)
                    setFragmentResult("from_edit", bundle)
                    NavHostFragment.findNavController(this).popBackStack()
                }
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
    }

}