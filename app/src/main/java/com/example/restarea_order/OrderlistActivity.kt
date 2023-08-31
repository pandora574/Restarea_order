package com.example.restarea_order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.restarea_order.databinding.ActivityOrderlistBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OrderlistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderlistBinding
    private val db = FirebaseFirestore.getInstance()
    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setTitle("주문하기")

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        var sum = 0
        db.collection("cart")
            .whereEqualTo("uid", currentUserUid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot){
                    val priceStr = document["price"] as String
                    val priceInt = priceStr.toInt()
                    sum += priceInt
                }
                binding.TextviewPayment.text = sum.toString()
            }
            .addOnFailureListener { e ->
                // 검색 실패 시 동작
            }

        binding.ButtonOrder.setOnClickListener {
            var intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("sum",sum)
            startActivity(intent)
        }
    }
}
