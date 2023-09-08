package com.example.restarea_order.Activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentTransaction
import com.example.restarea_order.Fragment.HistoryFragment
import com.example.restarea_order.R
import com.example.restarea_order.databinding.ActivityOrderlistBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OrderlistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderlistBinding
    private val db = FirebaseFirestore.getInstance()
    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //
        val addActivity = AddActivity.addActivity

        binding.toolbar.setTitle("주문하기")

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        //버튼 비활성화
        binding.ButtonOrder.isEnabled = false

        //총합계 구하기
        var sum = 0
        db.collection("cart")
            .whereEqualTo("uid", currentUserUid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val priceStr = document["price"] as String
                    val priceInt = priceStr.toInt()
                    sum += priceInt
                }
                binding.TextviewPayment.text = sum.toString()
            }

        //예상수령시간 time에 저장하고 기타 입력받기(라디오 버튼)
        var time = ""
        binding.Radiogroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.Radiobutton_10 -> {
                    time = "10분"
                    binding.EdittextTime.visibility = View.GONE
                    Log.d("time10", time.toString())
                }
                R.id.Radiobutton_15 -> {
                    time = "15분"
                    binding.EdittextTime.visibility = View.GONE
                    Log.d("time15", time.toString())
                }
                R.id.Radiobutton_20 -> {
                    time = "20분"
                    binding.EdittextTime.visibility = View.GONE
                    Log.d("time20", time.toString())
                }
                R.id.Radiobutton_25 -> {
                    time = "25분"
                    binding.EdittextTime.visibility = View.GONE
                    Log.d("time25", time.toString())
                }
                R.id.Radiobutton_30 -> {
                    time = "30분"
                    binding.EdittextTime.visibility = View.GONE
                    Log.d("time30", time.toString())
                }
                R.id.Radiobutton_etc -> {
                    time = "기타"
                    binding.EdittextTime.visibility = View.VISIBLE
                    Log.d("time_etc", time.toString())
                }
            }
            binding.ButtonOrder.isEnabled = true
        }

        // 스피너에 들어갈 데이터 생성
        var spinner_data = resources.getStringArray(R.array.order_list)
        // 스피너 어뎁터 생성
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spinner_data)
        //스피너에 어뎁터 연결
        binding.spinner.adapter = adapter
        //결제방법 데이터 넣기(스피너)
        var order_method = ""
        //스피너 선택 이벤트
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> order_method = "신용/체크카드"
                    1 -> order_method = "휴대폰결제"
                    2 -> order_method = "토스페이"
                    3 -> order_method = "카카오페이"
                    4 -> order_method = "만나서결제"
                }
                Log.d("결제방법", order_method)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        //결제하기 버튼 눌렀을 때
        binding.ButtonOrder.setOnClickListener {
            //요청사항 입력받기
            val user_request = binding.EdittextCall.text.toString()
            Log.d("요청사항",user_request)

            //현재시간 가져오기
            val order_time: LocalDateTime = LocalDateTime.now()
            val order_time_format = order_time.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")) //형식 지정
            Log.d("시간", order_time_format.toString())

            //파이어스토어에 orderlist -> uid -> uid -> 날짜로 저장
            val db_ref = db.collection("orderlist").document(currentUserUid)
            val da_store = db_ref.collection(currentUserUid).document(order_time_format)

            val order_Map = HashMap<String,Any>()

            //장바구니에 있는 상품 메뉴 이름 가져와서 menu에 저장(배열)
            var menu_name = ArrayList<String>()
            db.collection("cart").whereEqualTo("uid", currentUserUid)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) { //document: 자동아이디
                        //리사이클러 뷰에 가져온 데이터를 넣을 때는 as를 사용해서 형변환해야함
                        val item = (
                            document["menu"] as String
                        )
                        menu_name.add(item)
                    }

                    //수령시간에 기타를 체크했을 경우 -> edittext값 가져오기
                    if (time == "기타") {
                        if (binding.EdittextTime.text.toString().trim().isNotEmpty()) {  // edittext창에 값이 있는 경우
                            time = binding.EdittextTime.text.toString()

                            //HistoryFragment로 인텐트 코드 추가하기

                            val userinfo = hashMapOf(
                                "sum" to sum.toString(), //총합계
                                "time" to time, //수령시간
                                "menu" to menu_name, //주문메뉴
                                "order_method" to order_method, //결제방법
                                "order_time" to order_time_format, //결제시간
                                "uid" to currentUserUid,
                                "request" to user_request //요청사항
                            )
                            order_Map[order_time_format] = userinfo
                            db_ref.get()
                                .addOnSuccessListener { documentSnapshot ->
                                    if (documentSnapshot.exists()) {
                                        db_ref.update(order_Map)
                                    } else {
                                        db_ref.set(order_Map)
                                    }
                                }

                            //결제가 되면 장바구니 목록 삭제
                            db.collection("cart")
                                .whereEqualTo("uid", currentUserUid)
                                .get()
                                .addOnSuccessListener { querySnapshot ->
                                    for (document in querySnapshot) {
                                        db.collection("cart").document(document.id)
                                            .delete()
                                    }
                                }
                        } else {
                            Toast.makeText(this, "예상 수령 시간을 입력해주세요", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        //인텐트 코트 추가하기

                        val userinfo = hashMapOf(
                            "sum" to sum.toString(),
                            "time" to time,
                            "menu" to menu_name,
                            "order_method" to order_method,
                            "order_time" to order_time_format,
                            "uid" to currentUserUid,
                            "request" to user_request
                        )
                        order_Map[order_time_format] = userinfo
                        db_ref.get()
                            .addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    db_ref.update(order_Map)
                                } else {
                                    db_ref.set(order_Map)
                                }
                            }
                        //장바구니에서 삭제
                        db.collection("cart")
                            .whereEqualTo("uid", currentUserUid)
                            .get()
                            .addOnSuccessListener { querySnapshot ->
                                for (document in querySnapshot) {
                                    db.collection("cart").document(document.id)
                                        .delete()

                                }
                            }
                    }
                }
            finish()
            if (addActivity != null) {
                addActivity.finish()
            }
        }
    }
}
