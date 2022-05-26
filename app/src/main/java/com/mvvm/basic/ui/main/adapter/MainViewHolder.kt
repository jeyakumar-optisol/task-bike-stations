package com.mvvm.basic.ui.main.adapter

import android.location.Location
import android.util.Log
import com.example.easywaylocation.EasyWayLocation
import com.mvvm.basic.databinding.ItemBikeStationsBinding
import com.mvvm.basic.domain.model.bike_station.ResponseBikeStations
import com.mvvm.basic.support.base.BaseViewHolder

class MainViewHolder(
    private val binding: ItemBikeStationsBinding,
    selectionList: List<Int>,
) : BaseViewHolder<ResponseBikeStations.Feature, Int>(selectionList, binding.root) {

    override fun bind(position: Int, item: ResponseBikeStations.Feature) {
        binding.feature = item.parcelize()
        //binding.distanceAppCompatTextView.setText()
        MainAdapter.CURRENT_GPS_LOCATION?.let { currentLocation ->
            val endLocation = Location("LocationB")
            endLocation.latitude = item.geometry.coordinates[0]
            endLocation.longitude = item.geometry.coordinates[1]

            val distance = EasyWayLocation.calculateDistance(
                currentLocation.latitude,
                currentLocation.longitude,
                endLocation.latitude,
                endLocation.longitude
            )

            Log.d("MainViewHolder", "distance: $distance")
            binding.distanceAppCompatTextView.text = "${distance.toInt()}m"
        }
    }
}