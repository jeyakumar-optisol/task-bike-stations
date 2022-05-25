package com.mvvm.basic.data.repository.remote.api

import com.mvvm.basic.domain.datasources.remote.api.RestDataSource
import com.mvvm.basic.domain.datasources.remote.api.RestService
import com.mvvm.basic.domain.model.bike_station.ResponseBikeStations
import javax.inject.Inject

class RestRepository @Inject constructor(private val restService: RestService) : RestDataSource {

    override suspend fun bikeStations(): Result<ResponseBikeStations> {
        return restService.bikeStations()
    }
}