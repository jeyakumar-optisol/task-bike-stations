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
import com.mvvm.basic.support.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@HiltViewModel
open class MainViewModel @Inject constructor(application: Application) :
    BaseViewModel(application) {
    @Inject
    lateinit var restDataSource: RestDataSource

    val liveDataBikeStations = MutableLiveData<List<ResponseBikeStations.Feature>>()

    override fun onCreate() {
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
}