package com.example.replybot.viewModels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.replybot.data.model.User
import com.example.replybot.service.AuthService
import com.example.replybot.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authService: AuthService) : BaseViewModel(){

    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()

    fun register(id:String,name:String,email:String,pass:String,conPass:String){
        Log.d("ewqewqewq","$name,$email,$pass,$conPass,$id")
        if(Utils.validate(name,email,pass,conPass) && pass==conPass){
            viewModelScope.launch {
                safeApiCall {
                    authService.createUser(
                        User(id,name,email,pass)
                    )
                }
                finish.emit(Unit)
            }
        }else {
            viewModelScope.launch {
                error.emit("Please provide all the information")
            }
        }
    }
}