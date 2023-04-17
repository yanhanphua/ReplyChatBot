package com.example.replybot.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.replybot.data.model.Rules
import com.example.replybot.repository.UserRepository
import com.example.replybot.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRulesViewModel@Inject constructor(private val userRepository: UserRepository): BaseViewModel() {
    val rule:MutableLiveData<Rules?> = MutableLiveData()

    fun getCurrentRule(id:String){
        viewModelScope.launch {
            val res = userRepository.getCurrentRules(id)
            rule.value = res
        }
    }

    fun updateRules(id:String,rules:Rules,originalRules: Rules?){
        viewModelScope.launch {
            userRepository.editRules(id,rules,originalRules)
        }
    }

}