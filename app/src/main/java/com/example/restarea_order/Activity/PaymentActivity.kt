package com.example.restarea_order.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.restarea_order.databinding.ActivityOrderlistBinding
import com.example.restarea_order.databinding.ActivityPaymentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    val db_store = FirebaseFirestore.getInstance()
    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setTitle("주문완료")

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}