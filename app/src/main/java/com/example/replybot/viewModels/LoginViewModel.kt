package com.example.replybot.viewModels

import androidx.lifecycle.viewModelScope
import com.example.replybot.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authService: AuthService):BaseViewModel() {

    val loginFinish: MutableSharedFlow<Unit> = MutableSharedFlow()

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            val res = safeApiCall { authService.login(email, pass) }
            if (res != null) {
                loginFinish.emit(Unit)
            } else {
                error.emit("Login failed")
            }
        }
    }
}