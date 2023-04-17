package com.example.replybot.viewModels

import androidx.lifecycle.MutableLiveData
import com.example.replybot.data.model.Rules

abstract class BaseRulesViewModel:BaseViewModel() {
    val allRules: MutableLiveData<List<Rules>> = MutableLiveData()
}