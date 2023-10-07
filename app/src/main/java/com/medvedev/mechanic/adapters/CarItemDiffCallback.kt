package com.medvedev.mechanic.adapters

import androidx.recyclerview.widget.DiffUtil
import com.medvedev.mechanic.activity.cars.Car

class CarItemDiffCallback : DiffUtil.ItemCallback<Car>() {
    override fun areItemsTheSame(oldItem: Car, newItem: Car): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Car, newItem: Car): Boolean {
        return oldItem == newItem
    }
}