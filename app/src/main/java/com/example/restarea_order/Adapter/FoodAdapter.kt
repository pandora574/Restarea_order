package com.example.restarea_order.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restarea_order.Activity.DetailActivity
import com.example.restarea_order.Data.Food
import com.example.restarea_order.Data.Header
import com.example.restarea_order.Data.ParentData
import com.example.restarea_order.R

class FoodAdapter(var itemList: ArrayList<ParentData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemViewType(position: Int): Int {
        return itemList[position].getType()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType == ParentData.TYPE_HEADER){
            ViewHolderHeader(
                LayoutInflater.from(parent.context).inflate(R.layout.header_view,parent,false)
            )
        }else{
            ViewHolderContent(
                LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item,parent,false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolderHeader){
            holder.bind(itemList[position] as Header)
        }else if(holder is ViewHolderContent){
            holder.bind(itemList[position] as Food)
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    fun addItem(itemList: ArrayList<ParentData>){
        this.itemList = itemList
    }


    inner class ViewHolderHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val headerView: TextView = itemView.findViewById(R.id.headerView)
        @SuppressLint("SetTextI18n")
        fun bind(headerData: Header){
            headerView.text = "${headerData.category}"
        }
    }

    inner class ViewHolderContent(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tv_name: TextView = itemView.findViewById(R.id.tv_name)
        val tv_price: TextView = itemView.findViewById(R.id.tv_price)
        val tv_data: TextView = itemView.findViewById(R.id.tv_data)

        init {
            itemView.setOnClickListener {
                val foodItem = itemList[adapterPosition] as Food // 현재 위치의 아이템 가져오기
                Intent(itemView.context, DetailActivity::class.java).apply {
                    putExtra("info", foodItem)
                    itemView.context.startActivity(this) // Intent 실행
                }
            }
        }
        fun bind(Food: Food){
            tv_name.text = Food.name
            tv_price.text = Food.price
            tv_data.text = Food.data

            }
        }

    }
