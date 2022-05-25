package com.mvvm.basic.domain.datasources.remote.api

import com.mvvm.basic.domain.model.bike_station.ResponseBikeStations

interface RestDataSource {

    suspend fun bikeStations(): Result<ResponseBikeStations>
}