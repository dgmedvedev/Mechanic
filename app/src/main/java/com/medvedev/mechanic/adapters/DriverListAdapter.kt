package com.medvedev.mechanic.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.medvedev.mechanic.R
import com.medvedev.mechanic.activity.drivers.Driver

class DriverListAdapter(private var items: List<Driver>) :
    RecyclerView.Adapter<DriverListViewHolder>() {

    var onDriverClickListener: OnDriverClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverListViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_driver, parent, false)

        return DriverListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holderDriver: DriverListViewHolder, position: Int) {
        holderDriver.apply {
            bind(items[position])
            itemView.setOnClickListener {
                onDriverClickListener?.onItemClick(items[position])
            }
        }
    }

    fun updateList(filterList: MutableList<Driver>) {
        items = filterList
        notifyDataSetChanged()
    }

    interface OnDriverClickListener {
        fun onItemClick(item: Driver)
    }
}