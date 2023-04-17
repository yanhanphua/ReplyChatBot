package com.example.replybot.data.model

data class Rules (
    val id:String="",
    val name:String="",
    val firstOption:String="",
    val secondOption:String="",
    val activation:Boolean=true,
    val reply:String = ""
)