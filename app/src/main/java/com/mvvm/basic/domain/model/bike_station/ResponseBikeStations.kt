package com.mvvm.basic.domain.model.bike_station


import androidx.annotation.Keep

@Keep
class ResponseBikeStations(
    var features: List<Feature>
){ @Keep
    class Geometry(
        var coordinates: List<Double>,
        var type: String
    )

    @Keep
    class Feature(
        var geometry: Geometry
    )
}