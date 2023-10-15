package com.medvedev.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.medvedev.presentation.pojo.Car

class CarItemDiffCallback : DiffUtil.ItemCallback<Car>() {
    override fun areItemsTheSame(oldItem: Car, newItem: Car): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Car, newItem: Car): Boolean {
        return oldItem == newItem
    }
}