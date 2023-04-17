package com.example.replybot.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.replybot.data.model.Rules
import com.example.replybot.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RulesViewModel @Inject constructor(private val userRepository: UserRepository): BaseRulesViewModel(){
    private val _refreshActivatedRules:MutableLiveData<Boolean> = MutableLiveData(false)
    val refreshActivatedRules:LiveData<Boolean> = _refreshActivatedRules
    private val _refreshDeactivatedRules:MutableLiveData<Boolean> = MutableLiveData(false)
    val refreshDeactivatedRules:LiveData<Boolean> = _refreshDeactivatedRules



    fun onRefreshActivatedRules(refresh:Boolean){
        viewModelScope.launch {
            Log.d("ewqewewq","does it work")
            _refreshActivatedRules.value = refresh

        }
    }
    fun onRefreshDeactivatedRules(refresh:Boolean){
        viewModelScope.launch {
            _refreshDeactivatedRules.value=refresh
        }
    }

}