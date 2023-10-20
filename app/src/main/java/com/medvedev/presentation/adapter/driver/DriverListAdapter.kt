package com.medvedev.presentation.adapter.driver

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.medvedev.presentation.pojo.Driver
import com.medvedev.mechanic.databinding.ItemDriverBinding

class DriverListAdapter :
    ListAdapter<Driver, DriverListAdapter.DriverListViewHolder>(DriverDiffCallback()) {

    var onDriverClickListener: OnDriverClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverListViewHolder {
        val binding = ItemDriverBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return DriverListViewHolder(binding)
    }

    override fun onBindViewHolder(holderDriver: DriverListViewHolder, position: Int) {

        val driverItem = getItem(position)
        val binding = holderDriver.binding

        binding.apply {
            // loadRoundImage(item.imageUrl, imageView) в разработке
            firstNameTextView.text = driverItem.name
            secondNameTextView.text = driverItem.surname
            vuSrokTextView.text = driverItem.drivingLicenseValidity
            msSrokTextView.text = driverItem.medicalCertificateValidity
            root.setOnClickListener {
                onDriverClickListener?.onItemClick(driverItem)
            }
        }
    }

    interface OnDriverClickListener {
        fun onItemClick(item: Driver)
    }

    class DriverListViewHolder(val binding: ItemDriverBinding) :
        RecyclerView.ViewHolder(binding.root)
}