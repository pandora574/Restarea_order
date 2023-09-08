package com.example.restarea_order.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restarea_order.Adapter.FoodAdapter
import com.example.restarea_order.Data.Food
import com.example.restarea_order.Data.Header
import com.example.restarea_order.Data.ParentData
import com.example.restarea_order.databinding.FragmentProductBinding
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class ProductFragment : Fragment() {

    private var binding: FragmentProductBinding? = null

    val db_store = FirebaseFirestore.getInstance()// Firestore 인스턴스 선언

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        val context = context
        val dividerItemDecoration =
            DividerItemDecoration(
                binding?.rvBoard?.context,
                LinearLayoutManager(context).orientation
            )

        binding?.rvBoard?.addItemDecoration(dividerItemDecoration) //경계선

        var contentList = ArrayList<Food>()

        db_store.collection("서부산휴게소").get()
            .addOnSuccessListener { result ->
                contentList.clear()
                for (document in result) { //document: 자동아이디
                    //리사이클러 뷰에 가져온 데이터를 넣을 때는 as를 사용해서 형변환해야함
                    val item = Food(
                        document["category"] as String,
                        document["menu"] as String,
                        document["price"] as String,
                        document["data"] as String
                    )
                    contentList.add(item)
                }

        var headerList = ArrayList<Header>()
                headerList.clear()
                for (content in contentList) {
                    val category = content.category
                    val isNewCategory = headerList.none { it.category == category }

                    if (isNewCategory) {
                        headerList.add(Header(category))
                    }
                }
                val reverseComparator = compareByDescending<Header> { it.category }
                headerList.sortWith(reverseComparator)

        var parentList = ArrayList<ParentData>()
        parentList.clear()
        for (i in 0 until headerList.size) {
            parentList.add(headerList[i])
            for (j in 0 until contentList.size) {
                if (contentList[j].category == headerList[i].category) {
                    parentList.add(contentList[j])
                }
            }
        }

                Log.d("parentList", parentList.toString())
                val category_list : ArrayList<Int> = arrayListOf()
                for (header in headerList){
                    var menu_category = parentList.indexOfFirst { it is Header && it.category == header.category }
                    category_list.add(menu_category)

                }
                Log.d("category_list", category_list.toString())

                var chip1 = Chip(context)
                chip1.text = "전체"
                chip1.isClickable = true
                chip1.setOnClickListener {
                    val linearLayoutManager =
                        binding!!.rvBoard.getLayoutManager() as LinearLayoutManager
                    linearLayoutManager?.scrollToPositionWithOffset(0, 0)//리사이클러뷰 특정 위치가 맨위로 이동
                }
                binding!!.chipGroup.addView(chip1)

                for (i in 0 until headerList.size){
                    val chip = Chip(context)
                    chip.text = headerList[i].category
                    chip.isClickable = true

                    chip.setOnClickListener {
                        val linearLayoutManager =
                            binding!!.rvBoard.getLayoutManager() as LinearLayoutManager

                        linearLayoutManager?.scrollToPositionWithOffset(category_list[i], 0)//리사이클러뷰 특정 위치가 맨위로 이동
                    }
                    binding!!.chipGroup.addView(chip)//chip생성
                }

        binding!!.rvBoard.layoutManager = LinearLayoutManager(context)
        binding!!.rvBoard.apply {
            setHasFixedSize(true)
            var foodAdapter = FoodAdapter(parentList)
            Log.d("parentList", parentList.toString())
            adapter = foodAdapter
            foodAdapter.notifyDataSetChanged()
        }
    }
        return binding?.root
    }
}

