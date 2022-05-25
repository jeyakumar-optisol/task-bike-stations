package com.mvvm.basic.support

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object CommonUtility {
    fun runOnNewThread(callback: suspend CoroutineScope.() -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            callback()
        }
    }

    fun CoroutineScope.runOnUiThread(callback: suspend CoroutineScope.() -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            callback()
        }
    }

    fun AppCompatActivity.selfCheckPermission(permissionName: String): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            val granted = ContextCompat.checkSelfPermission(this, permissionName)
            granted == PackageManager.PERMISSION_GRANTED
        } else {
            val granted = PermissionChecker.checkSelfPermission(this, permissionName)
            granted == PermissionChecker.PERMISSION_GRANTED
        }
    }
}