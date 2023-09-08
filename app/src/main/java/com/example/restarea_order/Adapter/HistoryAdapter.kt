package com.example.restarea_order.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restarea_order.Data.History
import com.example.restarea_order.R

class HistoryAdapter (var items: ArrayList<History>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.product_name.text = items[position].menu
        holder.date.text = items[position].order_time
        holder.method.text = items[position].method
        holder.sum.text = items[position].sum
        holder.request.text = items[position].request
        holder.time.text = items[position].time

    }

    override fun getItemCount(): Int {
        return  items.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        //var restarea_name: TextView = itemView.findViewById(R.id.tv_restarea)
        var product_name: TextView = itemView.findViewById(R.id.tv_menu)
        var date: TextView = itemView.findViewById(R.id.tv_data)
        var method: TextView = itemView.findViewById(R.id.tv_method)
        var sum: TextView = itemView.findViewById(R.id.tv_sum)
        var request: TextView = itemView.findViewById(R.id.tv_request)
        var time: TextView = itemView.findViewById(R.id.tv_time)
    }
}