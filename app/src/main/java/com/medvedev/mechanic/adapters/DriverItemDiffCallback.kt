package com.medvedev.mechanic.adapters

import androidx.recyclerview.widget.DiffUtil
import com.medvedev.mechanic.activity.drivers.Driver

class DriverItemDiffCallback : DiffUtil.ItemCallback<Driver>() {
    override fun areItemsTheSame(oldItem: Driver, newItem: Driver): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Driver, newItem: Driver): Boolean {
        return oldItem == newItem
    }
}