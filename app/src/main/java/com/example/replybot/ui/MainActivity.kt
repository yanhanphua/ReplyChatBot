package com.example.replybot.ui

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.replybot.R
import com.example.replybot.receiver.MyBroadcastReceiver
import com.example.replybot.service.AuthService
import com.example.replybot.service.MyService
import com.example.replybot.service.NotificationService
import com.example.replybot.utils.Constants.DEBUG
import com.example.replybot.utils.NotificationUtils
import com.google.android.gms.auth.api.phone.SmsRetriever
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private val NOTIFICATION_REQ_CODE = 0
    private val FOREGROUND_REQ_CODE = 1
    private lateinit var myService: MyService
    private lateinit var myReceiver: MyBroadcastReceiver

    @Inject
    lateinit var authService: AuthService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        myReceiver=MyBroadcastReceiver()

        NotificationUtils.createNotificationChannel(this)
        checkPermission("android.permission.POST_NOTIFICATIONS", NOTIFICATION_REQ_CODE)
        checkPermission("android.permission.FOREGROUND_SERVICE", FOREGROUND_REQ_CODE)
        if (!authService.isAuthenticate()) {
            findNavController(R.id.navHostFragment).navigate(R.id.loginFragment)
        } else {
            findNavController(R.id.navHostFragment).navigate(R.id.rulesFragment)
        }

        startService(Intent(this, NotificationService::class.java))
        startService()
    }

    fun startService() {
        serviceIntent().also {
            intent.putExtra("EXTRA_DATA", "Hello from MainActivity")
            startService(it)
        }
    }

    fun stopService() {
        serviceIntent().also {
            stopService(it)
        }
    }

    private fun serviceIntent(): Intent {
        myService = MyService()
        return Intent(this, MyService::class.java)
    }
    private fun resultLauncher() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val data = it.data
                    data?.let {
                        val msg = it.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE).toString()
                        val otp = Regex("\\d{4,6}").find(msg)?.value ?: ""
                    }
                }
            }
    }

    private fun registerBroadcastReceiver() {
        NotificationUtils.createNotificationChannel(this)
        checkPermission(
            "android.permission.POST_NOTIFICATIONS",
            NOTIFICATION_REQ_CODE
        )
        checkPermission(
            "android.permission.FOREGROUND_SERVICE",
            FOREGROUND_REQ_CODE
        )

        val filter = IntentFilter()
        filter.addAction("com.replyBot.MyBroadcast")

        registerReceiver(myReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(myReceiver)
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this, permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else {
            Log.d(DEBUG, "Permission granted from MainActivity")
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            NOTIFICATION_REQ_CODE -> {
                makeToast("Notification permission granted")
            }

            FOREGROUND_REQ_CODE -> {
                makeToast("Foreground service permission granted")
            }
            else -> {
                makeToast("Permission Denied")
            }
        }
    }

    fun makeToast(reply: String) {
        return Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
    }
}