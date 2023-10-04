package com.medvedev.mechanic.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.medvedev.mechanic.R
import com.medvedev.mechanic.activity.cars.Car

class CarListAdapter(private var items: List<Car>) :
    RecyclerView.Adapter<CarListViewHolder>() {

    var onCarClickListener: OnCarClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_car, parent, false)

        return CarListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holderCar: CarListViewHolder, position: Int) {
        holderCar.bind(items[position])

        holderCar.itemView.setOnClickListener {
            onCarClickListener?.onItemClick(items[position])
        }
    }

    fun updateList(filterList: MutableList<Car>) {
        items = filterList
        notifyDataSetChanged()
    }

    interface OnCarClickListener {
        fun onItemClick(item: Car)
    }
}