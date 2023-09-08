package com.example.restarea_order.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restarea_order.Adapter.AddAdapter
import com.example.restarea_order.Data.OrderList
import com.example.restarea_order.databinding.ActivityAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    val db_store = FirebaseFirestore.getInstance()
    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

    companion object{
        // MainActivity 타입의 객체를 동반 객체로 선언한다(자바에서는 static)
        var addActivity : AddActivity? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setTitle("장바구니")
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        Log.d("userid", currentUserUid.toString())

        addActivity = this

        val dividerItemDecoration =
            DividerItemDecoration(
                binding!!.rvBoard.context,
                LinearLayoutManager(this).orientation
            )

        binding!!.rvBoard.addItemDecoration(dividerItemDecoration)

        var orderList = ArrayList<OrderList>()


        db_store.collection("cart").whereEqualTo("uid",currentUserUid.toString())
            .get()
            .addOnSuccessListener { result ->
                for (document in result) { //document: 자동아이디
                    //리사이클러 뷰에 가져온 데이터를 넣을 때는 as를 사용해서 형변환해야함
                       val item = OrderList(
                            //document["restarea"] as String,
                            document["menu"] as String,
                            document["price"] as String,
                            document["num"] as String,
                           document["primecost"] as String
                        )
                    Log.d("item", item.toString())
                        orderList.add(item)}
                Log.d("orderList", orderList.toString())

                binding!!.rvBoard.layoutManager = LinearLayoutManager(this)
                binding!!.rvBoard.apply {
                    setHasFixedSize(true)
                    var addAdapter = AddAdapter(orderList)
                    Log.d("orderList", orderList.toString())
                    adapter = addAdapter
                    addAdapter.notifyDataSetChanged()
                }
                }

        binding?.ButtonOrder?.setOnClickListener {
            var intent = Intent(this, OrderlistActivity::class.java)
            startActivity(intent)
        }

    }
}