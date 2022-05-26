package com.mvvm.basic.ui.view_station

import android.app.Application
import com.mvvm.basic.domain.datasources.remote.api.RestDataSource
import com.mvvm.basic.support.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class ViewStationViewModel @Inject constructor(application: Application) :
    BaseViewModel(application) {

    @Inject
    lateinit var restDataSource: RestDataSource

    /*manually triggered from when viewmodel attached to activity*/
    override fun onCreate() {
        //noop
    }

}