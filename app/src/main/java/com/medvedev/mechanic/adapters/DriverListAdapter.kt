package com.medvedev.mechanic.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.medvedev.mechanic.R
import com.medvedev.mechanic.activity.drivers.Driver

class DriverListAdapter(private var items: List<Driver>, private val listener: ClickListener) :
    RecyclerView.Adapter<DriverListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_driver, parent, false)

        val holder = DriverListViewHolder(view)

        holder.itemView.setOnClickListener {
            listener.onItemClick(items[holder.adapterPosition])
        }
        return holder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holderCar: DriverListViewHolder, position: Int) {
        holderCar.bind(items[position])
    }

    fun updateList(filterList: MutableList<Driver>) {
        items = filterList
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun onItemClick(item: Driver)
    }
}