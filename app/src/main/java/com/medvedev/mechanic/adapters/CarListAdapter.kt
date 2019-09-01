package com.medvedev.mechanic.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.medvedev.mechanic.R
import com.medvedev.mechanic.activity.cars.Car

class CarListAdapter(private var items: List<Car>, private val listener: ClickListener) :
    RecyclerView.Adapter<CarListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_car, parent, false)

        val holder = CarListViewHolder(view)

        holder.itemView.setOnClickListener {
            listener.onItemClick(items[holder.adapterPosition])
        }
        return holder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holderCar: CarListViewHolder, position: Int) {
        holderCar.bind(items[position])
    }

    fun updateList(filterList: MutableList<Car>) {
        items = filterList
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun onItemClick(item: Car)
    }
}