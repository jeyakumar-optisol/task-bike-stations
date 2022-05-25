package com.mvvm.basic.domain.datasources.remote.api

import com.mvvm.basic.domain.model.bike_station.ResponseBikeStations
import retrofit2.http.GET

interface RestService {
    @GET("/mim/plan/map_service.html?mtype=pub_transport&co=stacje_rowerowe")
    suspend fun bikeStations(): Result<ResponseBikeStations>
}