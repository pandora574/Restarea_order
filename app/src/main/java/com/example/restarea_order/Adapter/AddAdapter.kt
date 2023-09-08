package com.example.restarea_order.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restarea_order.Data.OrderList
import com.example.restarea_order.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AddAdapter (var items: ArrayList<OrderList>) : RecyclerView.Adapter<AddAdapter.ViewHolder>(){

    val db = FirebaseFirestore.getInstance()
    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add,parent,false)
            return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //holder.restarea_name.text = items[position].restarea_name
        holder.product_name.text = items[position].name
        holder.product_num.text = items[position].num
        holder.primecost.text = items[position].primecost
        holder.product_price.text = (items[position].price.toString().toInt()).toString()

        holder.product_del.setOnClickListener {
            val clickedPosition = holder.adapterPosition
            if (clickedPosition != RecyclerView.NO_POSITION) {
                val deletedName = items[clickedPosition].name.toString()
                Log.d("delname",deletedName)
                remove(clickedPosition)

                db.collection("cart")
                    .whereEqualTo("uid", currentUserUid)
                    .whereEqualTo("menu", deletedName)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot) {
                            db.collection("cart").document(document.id)
                                .delete()
                                .addOnSuccessListener {
                                    // 삭제 성공 시 동작
                                }
                                .addOnFailureListener { e ->
                                    // 삭제 실패 시 동작
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        // 검색 실패 시 동작
                    }
            }
        }
        val currentItem = items[position]
        var deletedName = items[holder.adapterPosition].num.toString().toInt()
        var deletedPrice = items[holder.adapterPosition].primecost.toString().toInt()

        holder.tv_del.setOnClickListener {
            if (deletedName > 1) {
                deletedName--
                holder.product_num.text = deletedName.toString()

                deletedPrice = deletedName * (currentItem.primecost.toString().toInt())// 가격 계산
                holder.product_price.text = deletedPrice.toString()//가격 표시
                db.collection("cart")
                    .whereEqualTo("uid", currentUserUid)
                    .whereEqualTo("menu", currentItem.name)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot) {
                            val docId = document.id
                            db.collection("cart").document(docId)
                                .update(
                                    mapOf("num" to deletedName.toString(),
                                    "price" to deletedPrice.toString()))
                                .addOnSuccessListener {
                                    // 수정 성공 시 동작
                                }
                                .addOnFailureListener { e ->
                                    // 수정 실패 시 동작
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        // 검색 실패 시 동작
                    }
            }
        }
        holder.tv_plus.setOnClickListener {
            deletedName++
            holder.product_num.text = deletedName.toString()
            Log.d("더하기전",currentItem.primecost.toString())
            deletedPrice = deletedName * (currentItem.primecost.toString().toInt())
            holder.product_price.text = deletedPrice.toString()
            db.collection("cart")
                .whereEqualTo("uid", currentUserUid)
                .whereEqualTo("menu", currentItem.name)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        val docId = document.id
                        db.collection("cart").document(docId)
                            .update(
                                mapOf("num" to deletedName.toString(),
                                    "price" to deletedPrice.toString()))
                            .addOnSuccessListener {
                                // 수정 성공 시 동작
                            }
                            .addOnFailureListener { e ->
                                // 수정 실패 시 동작
                            }
                    }
                }
                .addOnFailureListener { e ->
                    // 검색 실패 시 동작
                }
        }

    }
    fun remove(position: Int) {
        if (position in 0 until items.size) {
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return  items.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var restarea_name: TextView = itemView.findViewById(R.id.tv_restarea)
        var product_name: TextView = itemView.findViewById(R.id.tv_name)
        var product_num: TextView = itemView.findViewById(R.id.tv_num)
        var primecost: TextView = itemView.findViewById(R.id.tv_primecost)
        var product_price: TextView = itemView.findViewById(R.id.tv_price)
        var product_del: ImageView = itemView.findViewById(R.id.btn_del)
        var tv_del: TextView = itemView.findViewById(R.id.tv_del)
        var tv_plus: TextView = itemView.findViewById(R.id.tv_plus)
    }
}