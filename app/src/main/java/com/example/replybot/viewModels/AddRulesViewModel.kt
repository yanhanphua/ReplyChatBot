package com.example.replybot.viewModels

import androidx.lifecycle.viewModelScope
import com.example.replybot.data.model.Rules
import com.example.replybot.repository.UserRepository
import com.example.replybot.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRulesViewModel@Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {

    fun addRules(id:String,rules:Rules){
        viewModelScope.launch {
            userRepository.addRules(id,rules)
        }
    }
}