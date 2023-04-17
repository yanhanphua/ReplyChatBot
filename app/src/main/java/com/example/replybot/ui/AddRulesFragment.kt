package com.example.replybot.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.replybot.R
import com.example.replybot.data.model.Rules
import com.example.replybot.databinding.FragmentAddRulesBinding
import com.example.replybot.databinding.FragmentRulesBinding
import com.example.replybot.service.AuthService
import com.example.replybot.viewModels.AddRulesViewModel
import com.example.replybot.viewModels.RegisterViewModel
import com.example.replybot.viewModels.RulesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddRulesFragment : BaseFragment<FragmentAddRulesBinding>() {

    override val viewModel: AddRulesViewModel by viewModels()

    @Inject
    lateinit var authService: AuthService

    override fun getLayoutResource() = R.layout.fragment_add_rules

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)

        binding?.let {binding ->
            binding.btnAdd.setOnClickListener {
                val id= UUID.randomUUID().toString()
                val name = binding.name.text.toString()
                val reply = binding.reply.text.toString()
                val option1 = binding.option1.text.toString()
                val option2 = binding.option2.text.toString()
                if(id.isEmpty() || name.isEmpty() || reply.isEmpty() || option1.isEmpty() || option2.isEmpty()){
                    val snackBar =
                        Snackbar.make(
                            binding.root,"Please enter all the values", Snackbar.LENGTH_LONG
                        )
                    snackBar.show()
                }else{
                    viewModel.addRules(authService.getUserUid().toString(),Rules(id,name, option1,option2,true,reply))
                    val bundle = Bundle()
                    bundle.putBoolean("refresh", true)
                    setFragmentResult("from_add_item", bundle)
                    NavHostFragment.findNavController(this).popBackStack()
                }
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
    }

}