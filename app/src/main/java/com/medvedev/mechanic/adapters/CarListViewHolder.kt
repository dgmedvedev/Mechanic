package com.medvedev.mechanic.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.medvedev.mechanic.R
import com.medvedev.mechanic.activity.cars.Car

class CarListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val brandTextView = itemView.findViewById<TextView>(R.id.brandTextView)
    private val modelTextView = itemView.findViewById<TextView>(R.id.modelTextView)
    private val regNumberTextView = itemView.findViewById<TextView>(R.id.regNumberTextView)
    private val yearProductionTextView = itemView.findViewById<TextView>(R.id.yearProductionTextView)

    fun bind(item: Car) {
        // loadRoundImage(item.imageUrl, imageView) в разработке
        brandTextView.text = item.brand
        modelTextView.text = item.model
        regNumberTextView.text = item.stateNumber
        yearProductionTextView.text = item.yearProduction.toString()
    }
}