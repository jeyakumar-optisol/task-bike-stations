package com.mvvm.basic.ui.main.adapter

import com.mvvm.basic.databinding.ItemBikeStationsBinding
import com.mvvm.basic.domain.model.bike_station.ResponseBikeStations
import com.mvvm.basic.support.base.BaseViewHolder

class MainViewHolder(
    private val binding: ItemBikeStationsBinding,
    selectionList: List<Int>,
) : BaseViewHolder<ResponseBikeStations.Feature, Int>(selectionList, binding.root) {

    override fun bind(position: Int, item: ResponseBikeStations.Feature) {
        binding.feature = item
    }
}