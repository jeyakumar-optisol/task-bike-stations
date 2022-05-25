package com.mvvm.basic.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mvvm.basic.databinding.ItemBikeStationsBinding
import com.mvvm.basic.domain.model.bike_station.ResponseBikeStations
import com.mvvm.basic.support.base.BaseAdapter2
import com.mvvm.basic.support.base.BaseViewHolder

class MainAdapter(private val itemListener: ItemListener? = null) :
    BaseAdapter2<ResponseBikeStations.Feature, Int>() {
    override fun getList(): List<ResponseBikeStations.Feature> {
        return currentList
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<ResponseBikeStations.Feature, Int> {
        return MainViewHolder(
            ItemBikeStationsBinding.inflate(LayoutInflater.from(parent.context)), getSelections()
        )
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ResponseBikeStations.Feature, Int>, position: Int
    ) {
        holder.bind(position, currentList[position])
        holder.itemView.setOnClickListener {
            itemListener?.onItemSelected(position, currentList[position])
        }
    }

    interface ItemListener {
        fun onItemSelection(position: Int, item: ResponseBikeStations.Feature) {
            //noop
        }

        fun onItemSelected(position: Int, item: ResponseBikeStations.Feature) {
            //noop
        }

        fun onOptionSelected(
            view: View? = null,
            position: Int,
            item: String,
        ) {
            //noop
        }
    }

    override fun isSameItem(
        oldItem: ResponseBikeStations.Feature, newItem: ResponseBikeStations.Feature
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun isSameContent(
        oldItem: ResponseBikeStations.Feature,
        newItem: ResponseBikeStations.Feature,
    ): Boolean {
        return oldItem.properties == newItem.properties
    }
}