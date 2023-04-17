package com.example.replybot.repository

import com.example.replybot.data.model.Rules
import com.example.replybot.data.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>

    suspend fun getAllRules():List<Rules>?

    suspend fun removeRules(id:String,rulesId:Rules)

    suspend fun editRules(id:String,rules:Rules,originalRules: Rules?)

    suspend fun activationRules(id:String,rules:Rules,originalRules: Rules?)

    suspend fun addRules(id:String,rule:Rules)

    suspend fun getCurrentRules(id:String):Rules?
}