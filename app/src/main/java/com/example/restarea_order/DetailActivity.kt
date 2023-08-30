package com.example.restarea_order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.restarea_order.databinding.ActivityDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
    val db_store = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        var datas = intent.getSerializableExtra("info") as Food
        Log.d("datas", datas.toString())

        binding.toolbar.setTitle("상품정보")

        binding.name.text = datas.name.toString()
        binding.price.text = datas.price.toString()
        binding.data.text = datas.data.toString()

        var num = 1

        binding.ButtonPlus.setOnClickListener {
            num++
            binding.TextviewNum.text = num.toString()
        }
        binding.ButtonDel.setOnClickListener {
            if (num == 1){
                binding.TextviewNum.text = num.toString()
            }else{
                num--
                binding.TextviewNum.text = num.toString()
            }
        }

        binding.ButtonAdd.setOnClickListener {
                val userinfo = hashMapOf(
                    "menu" to  datas.name.toString(),
                    "price" to datas.price.toString(),
                    "num" to num.toString(),
                    "uid" to currentUserUid,
                    "restarea" to "서부산휴게소"
                )

            val existingCartItem = db_store.collection("cart")
                .whereEqualTo("menu", datas.name.toString())
                .whereEqualTo("uid", currentUserUid)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        // 이미 장바구니에 있는 경우 수량 업데이트
                        val existingDocument = querySnapshot.documents[0]
                        val existingNum = existingDocument.getString("num")?.toInt() ?: 0
                        val updatedNum = existingNum + num
                        existingDocument.reference.update("num", updatedNum.toString())
                    } else {
                        // 장바구니에 없는 경우 새로 추가
                        db_store.collection("cart").add(userinfo)
                    }
                    Toast.makeText(this, "상품이 장바구니에 담겼습니다", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    // 검색 실패 시 동작
                }

            }

        }
    }

