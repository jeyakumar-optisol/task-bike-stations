package com.mvvm.basic.ui.main

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mvvm.basic.data.exception.CustomException
import com.mvvm.basic.domain.datasources.remote.api.RestDataSource
import com.mvvm.basic.domain.model.bike_station.ResponseBikeStations
import com.mvvm.basic.support.CommonUtility.runOnNewThread
import com.mvvm.basic.support.CommonUtility.runOnUiThread
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@HiltViewModel
open class MainViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {
    protected val context: Context get() = getApplication<Application>()

    @Inject
    lateinit var restDataSource: RestDataSource

    val liveDataLoader = MutableLiveData<Boolean>()
    val liveDataBikeStations = MutableLiveData<List<ResponseBikeStations.Feature>>()

    fun onCreate() {
        retrieveBikeStations { b, list ->
            if (b) {
                liveDataBikeStations.value = list
            }
        }
    }

    fun retrieveBikeStations(callback: (Boolean, List<ResponseBikeStations.Feature>) -> Unit) {
        runOnNewThread {
            showLoader()

            restDataSource.bikeStations().onSuccess {
                hideLoader()
                runOnUiThread {
                    liveDataBikeStations.value = it.features
                }

            }.onFailure {
                hideLoader()
                (it as CustomException).apply {
                    toast(this.getError())
                }
            }
        }
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