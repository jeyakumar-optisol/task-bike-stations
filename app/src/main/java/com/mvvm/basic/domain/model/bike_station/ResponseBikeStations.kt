package com.mvvm.basic.domain.model.bike_station


import android.os.Parcelable
import androidx.annotation.Keep

@Keep
class ResponseBikeStations(
    var features: List<Feature>
) {
    @Keep
    class Feature(
        var geometry: Geometry, var id: String, var properties: Properties, var type: String
    ) {
        fun parcelize(): Parcel {
            return Parcel(properties, geometry.coordinates[0], geometry.coordinates[1])
        }
    }

    @Keep
    class Properties(
        var bike_racks: String,
        var bikes: String,
        var free_racks: String,
        var label: String,
        var updated: String
    ) : Parcelable {
        constructor(parcel: android.os.Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: ""
        ) {
        }

        override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
            parcel.writeString(bike_racks)
            parcel.writeString(bikes)
            parcel.writeString(free_racks)
            parcel.writeString(label)
            parcel.writeString(updated)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Properties> {
            override fun createFromParcel(parcel: android.os.Parcel): Properties {
                return Properties(parcel)
            }

            override fun newArray(size: Int): Array<Properties?> {
                return arrayOfNulls(size)
            }
        }
    }

    @Keep
    class Geometry(
        var coordinates: List<Double>, var type: String
    )

    @Keep
    class Parcel(
        var properties: Properties?, var GeometryLat: Double, var geometryLng: Double
    ) : Parcelable {
        constructor(parcel: android.os.Parcel) : this(
            parcel.readParcelable(Properties::class.java.classLoader),
            parcel.readDouble(),
            parcel.readDouble()
        ) {
        }

        override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
            parcel.writeParcelable(properties, flags)
            parcel.writeDouble(GeometryLat)
            parcel.writeDouble(geometryLng)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Parcel> {
            override fun createFromParcel(parcel: android.os.Parcel): Parcel {
                return Parcel(parcel)
            }

            override fun newArray(size: Int): Array<Parcel?> {
                return arrayOfNulls(size)
            }
        }
    }

}