package com.example.replybot.repositories

import android.system.Os.remove
import android.util.Log
import com.example.replybot.data.model.Rules
import com.example.replybot.data.model.User
import com.example.replybot.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await

class UserRepositoryImp(private val auth: FirebaseAuth, private val ref: CollectionReference) :
    UserRepository {

    override suspend fun getUsers(): List<User> {
        val res = ref.get().await()
        val users = mutableListOf<User>()
        res.documents.forEach {
            it.toObject(User::class.java)?.let { user ->
                users.add(user)
            }
        }
        return users
    }

    override suspend fun getAllRules(): List<Rules>? {
        val user = auth.uid?.let {
            ref.document(it).get().await().toObject(User::class.java)
        }
        return user?.let {
            Log.d("gettingAllRules",user.rules.toString())
            user.rules
        }
    }

    override suspend fun addRules(id: String, rule: Rules) {
        ref.document(id).update("rules", FieldValue.arrayUnion(rule)).await()
    }

    override suspend fun removeRules(id: String, rulesId: Rules) {
        ref.document(id).update("rules", FieldValue.arrayRemove(rulesId))
    }

    override suspend fun editRules(id: String, rule: Rules,originalRule:Rules?) {
        ref.document(id).update("rules", FieldValue.arrayRemove(originalRule))
            .addOnSuccessListener {
                ref.document(id).update("rules", FieldValue.arrayUnion(rule)).addOnSuccessListener {
                    Log.d("ewqewqewq","how")
                }
            }
    }

    override suspend fun activationRules(id: String, rules: Rules, originalRules: Rules?) {
        ref.document(id).update("rules",FieldValue.arrayRemove(originalRules)).addOnSuccessListener {
            ref.document(id).update("rules",FieldValue.arrayUnion(rules)).addOnSuccessListener {
            }
        }
    }

    override suspend fun getCurrentRules(id: String): Rules? {
        val user = auth.uid?.let {
            ref.document(it).get().await().toObject(User::class.java)
        }
        val ruleList = user?.let {
            user.rules
        }
        ruleList?.let {
            for (rule in ruleList) {
                if (rule.id == id) {
                    return rule
                }
            }
        }
        return null
    }
}