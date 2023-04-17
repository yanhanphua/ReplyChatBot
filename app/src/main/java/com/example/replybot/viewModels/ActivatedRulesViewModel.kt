package com.example.replybot.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.replybot.data.model.Rules
import com.example.replybot.repository.UserRepository
import com.example.replybot.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivatedRulesViewModel @Inject constructor(private val userRepository: UserRepository) : BaseRulesViewModel(){
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()


    override fun onViewCreated() {
        super.onViewCreated()
        getRules()
    }

    fun getRules(){
        viewModelScope.launch {
            Log.d("ewqewqewq","getting rules")
            delay(1000)
            val rules = userRepository.getAllRules()
            Log.d("ewqewqewq",rules.toString())
            rules?.let{it ->
                allRules.value = it
            }
        }
    }
    fun removeRules(id:String,rulesId:Rules){
        viewModelScope.launch {
            userRepository.removeRules(id,rulesId)
        }
    }
    fun activationRules(id:String,rules: Rules,originalRules: Rules){
        viewModelScope.launch {
            userRepository.activationRules(id,rules,originalRules)
        }
    }
}