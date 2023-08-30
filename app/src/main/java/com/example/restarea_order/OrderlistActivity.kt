package com.example.restarea_order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.restarea_order.databinding.ActivityOrderlistBinding

class OrderlistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderlistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setTitle("주문하기")

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}