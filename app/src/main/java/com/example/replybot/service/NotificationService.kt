package com.example.replybot.service

import android.app.RemoteInput
import android.content.Intent
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.replybot.data.model.Rules
import com.example.replybot.data.model.WearableNotification
import com.example.replybot.repositories.UserRepositoryImp
import com.example.replybot.repository.UserRepository
import com.example.replybot.utils.Constants.DEBUG
import com.example.replybot.utils.NotificationUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import java.util.*

class NotificationService : NotificationListenerService() {
    private lateinit var intent: Intent
    lateinit var bundle: Bundle
    private lateinit var title: String
    private lateinit var wNotification: WearableNotification
    private lateinit var msg: String
    private var replyText: String = ""
    private lateinit var repo: UserRepositoryImp

    override fun onCreate() {
        super.onCreate()
        repo = UserRepositoryImp(FirebaseAuth.getInstance(), Firebase.firestore.collection("users"))
        start()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        Log.d(DEBUG, "Found a notification")

        wNotification = NotificationUtils.getWearableNotification(sbn) ?: return
        title = wNotification.bundle?.getString("android.title") ?: "Empty"
        Log.d(DEBUG, "Title: $title")

        if (!isRunning) return
        if (!checkTitle()) return

        checkMsg {
            createIntentBundle()
            wNotificationPendingIntent(sbn)
        }
    }

    private fun checkTitle(): Boolean {
        if (title.contains(
                Regex(
                    "caaron|ching|justin|yan|xiang|vikram|khayrul|601606|joel|quan|phua",
                    RegexOption.IGNORE_CASE
                )
            )
        ) {
            return true
        }
        return false
    }

    private fun checkMsg(callback: () -> Unit) {
        msg = wNotification.bundle?.getString("android.text") ?: "Empty"

        wNotification.bundle?.keySet()?.forEach {
            Log.d(DEBUG, "keySet: $it \n content: ${wNotification.bundle?.getString(it)}")
        }
        Log.d(DEBUG, wNotification.bundle?.keySet().toString())
        Log.d(DEBUG, "Message: $msg")
        val rules = getRules()
        var ruleFound = false

        for (i in rules) {

            if (msg.toLowerCase().contains(i.firstOption.toLowerCase()) && i.activation ||
                msg.toLowerCase().contains(i.secondOption.toLowerCase()) && i.activation
            ) {
                ruleFound = true
                Log.d("what tf", "bruh")
                replyText = i.reply
                break
            }
        }
        if (!ruleFound) return
        callback()
    }

    fun hasAppName(notifName: String, appName: String): Boolean {
        return notifName.contains(Regex(appName, RegexOption.IGNORE_CASE))
    }

    private fun getRules(): MutableList<Rules> {
        val rules: MutableList<Rules> = mutableListOf()

        val job = CoroutineScope(Dispatchers.Default).launch {
            val res = repo.getAllRules()
            res.let {
                it?.forEach { rule ->
                    rules.add(rule)
                }
            }
        }
        runBlocking {
            job.join()
        }
        return rules
    }

    private fun createIntentBundle() {
        intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        bundle = Bundle()
        Log.d("what tf", replyText)
        bundle.putCharSequence(wNotification.remoteInputs[0].resultKey, replyText)

        RemoteInput.addResultsToIntent(wNotification.remoteInputs.toTypedArray(), intent, bundle)
    }

    private fun wNotificationPendingIntent(sbn: StatusBarNotification?) {
        try {
            wNotification.pendingIntent?.let {
                CoroutineScope(Dispatchers.Default).launch {
                    isRunning = false
                    cancelNotification(sbn?.key)

                    it.send(this@NotificationService, 0, intent)
                    delay(500)
                    isRunning = true
                }
            }
        } catch (e: Exception) {
            isRunning = true
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        Log.d(DEBUG, "Destroyed")
        super.onDestroy()
    }

    companion object {
        private var isRunning: Boolean = false
        fun start() {
            isRunning = true
        }

        fun stop() {
            isRunning = false
        }
    }
}