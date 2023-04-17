package com.example.replybot.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.replybot.data.model.Rules
import kotlinx.coroutines.flow.MutableSharedFlow

abstract class BaseViewModel : ViewModel() {
    val error: MutableSharedFlow<String> = MutableSharedFlow()


    open fun onViewCreated() {

    }

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): T? {
        return try {
            apiCall.invoke()
        } catch (e: Exception) {
            error.emit(e.message.toString())
            null
        }
    }
}