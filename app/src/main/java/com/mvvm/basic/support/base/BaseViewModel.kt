package com.mvvm.basic.support.base

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mvvm.basic.support.CommonUtility.runOnUiThread
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

open class BaseViewModel constructor(application: Application) : AndroidViewModel(application) {
    protected val context: Context get() = getApplication<Application>()
    val liveDataLoader = MutableLiveData<Boolean>()

    open fun onCreate() {
    }

    fun CoroutineScope.showLoader() {
        runOnUiThread {
            liveDataLoader.value = true
        }
    }

    fun CoroutineScope.hideLoader() {
        runOnUiThread {
            liveDataLoader.value = false
        }
    }

    fun CoroutineScope.toast(string: String) {
        runOnUiThread {
            Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
        }
    }
}