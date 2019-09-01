package com.medvedev.mechanic.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.medvedev.mechanic.R
import com.medvedev.mechanic.activity.drivers.Driver

class DriverListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val firstNameTextView = itemView.findViewById<TextView>(R.id.firstNameTextView)
    private val secondNameTextView = itemView.findViewById<TextView>(R.id.secondNameTextView)
    private val vuSrokTextView = itemView.findViewById<TextView>(R.id.vuSrokTextView)
    private val msSrokTextView = itemView.findViewById<TextView>(R.id.msSrokTextView)

    fun bind(item: Driver) {
        // loadRoundImage(item.imageUrl, imageView) в разработке
        firstNameTextView.text = item.name
        secondNameTextView.text = item.surname
        vuSrokTextView.text = item.drivingLicenseValidity
        msSrokTextView.text = item.medicalCertificateValidity
    }
}