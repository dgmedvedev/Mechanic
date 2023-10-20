package com.medvedev.presentation.adapter.driver

import androidx.recyclerview.widget.DiffUtil
import com.medvedev.presentation.pojo.Driver

class DriverDiffCallback : DiffUtil.ItemCallback<Driver>() {
    override fun areItemsTheSame(oldItem: Driver, newItem: Driver): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Driver, newItem: Driver): Boolean {
        return oldItem == newItem
    }
}