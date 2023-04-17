package com.example.replybot.data.model

data class User(
    val id:String="",
    val username:String="",
    val email:String="",
    val password:String="",
    val rules:List<Rules> = listOf<Rules>()
)