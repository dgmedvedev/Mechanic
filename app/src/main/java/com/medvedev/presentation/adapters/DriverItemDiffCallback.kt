package com.medvedev.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.medvedev.presentation.pojo.Driver

class DriverItemDiffCallback : DiffUtil.ItemCallback<Driver>() {
    override fun areItemsTheSame(oldItem: Driver, newItem: Driver): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Driver, newItem: Driver): Boolean {
        return oldItem == newItem
    }
}