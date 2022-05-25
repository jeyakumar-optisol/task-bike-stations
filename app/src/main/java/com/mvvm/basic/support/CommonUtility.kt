package com.mvvm.basic.support

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
}