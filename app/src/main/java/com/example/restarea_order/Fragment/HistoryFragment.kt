package com.example.restarea_order.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restarea_order.Adapter.HistoryAdapter
import com.example.restarea_order.Data.History
import com.example.restarea_order.databinding.FragmentHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val db = FirebaseFirestore.getInstance()
    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        val context = context
        val dividerItemDecoration =
            DividerItemDecoration(
                binding?.rvBoard?.context,
                LinearLayoutManager(context).orientation
            )

        binding?.rvBoard?.addItemDecoration(dividerItemDecoration) //경계선

        var item = ArrayList<History>()
        val db_store = db.collection("orderlist").document(currentUserUid)
        db.collection("orderlist").document(currentUserUid).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val dataMap = documentSnapshot.data // Map<String, Any> 형태

                    if (dataMap != null) {
                        for ((key, value) in dataMap) {
                            val menuList = (value as Map<String, Any>)["menu"] as ArrayList<String>
                            val menuString = menuList.joinToString(", ") // 문자열로 변환
                            // 데이터를 적절한 형식으로 변환하여 리스트에 추가
                            val history = History(
                                (value as Map<String, Any>)["sum"] as String,
                                (value as Map<String, Any>)["request"] as String,
                                menuString,
                                (value as Map<String, Any>)["time"] as String,
                                (value as Map<String, Any>)["order_time"] as String,
                                (value as Map<String, Any>)["order_method"] as String

                            )
                            item.add(history)
                            Log.d("데이터 들어감",item.toString())
                        }
                    }

                    // 데이터를 RecyclerView에 바인딩하거나 원하는 작업을 수행할 수 있음
                } else {
                    // 문서가 존재하지 않는 경우 처리
                }

                binding!!.rvBoard.layoutManager = LinearLayoutManager(context)
                binding!!.rvBoard.apply {
                    setHasFixedSize(true)
                    var historyAdapter = HistoryAdapter(item)
                    adapter = historyAdapter
                    historyAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { e ->
                // 데이터를 가져오는 도중 에러가 발생한 경우 처리
            }


        return binding?.root
    }
}