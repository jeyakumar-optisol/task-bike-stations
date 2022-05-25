package com.mvvm.basic.domain.model.bike_station


import androidx.annotation.Keep

@Keep
class ResponseBikeStations(
    var features: List<Feature>
){
    @Keep
    class Feature(
        var geometry: Geometry,
        var id: String,
        var properties: Properties,
        var type: String
    )

    @Keep
    class Properties(
        var bike_racks: String,
        var bikes: String,
        var free_racks: String,
        var label: String,
        var updated: String
    )

    @Keep
    class Geometry(
        var coordinates: List<Double>,
        var type: String
    )
}