package com.medvedev.presentation.adapter.car

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.medvedev.mechanic.databinding.ItemCarBinding
import com.medvedev.presentation.pojo.Car

class CarListAdapter : ListAdapter<Car, CarListAdapter.CarListViewHolder>(CarDiffCallback()) {

    var onCarClickListener: ((Car) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarListViewHolder {
        val binding = ItemCarBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CarListViewHolder(binding)
    }

    override fun onBindViewHolder(holderCar: CarListViewHolder, position: Int) {
        val carItem = getItem(position)
        val binding = holderCar.binding

        binding.apply {
            // loadRoundImage(carItem.imageUrl, imageView) в разработке
            brandTextView.text = carItem.brand
            modelTextView.text = carItem.model
            regNumberTextView.text = carItem.stateNumber
            yearProductionTextView.text = carItem.yearProduction.toString()

            root.setOnClickListener {
                onCarClickListener?.invoke(carItem)
            }
        }
    }

    class CarListViewHolder(val binding: ItemCarBinding) : RecyclerView.ViewHolder(binding.root)
}