package com.example.restarea_order.Activity

import android.content.res.AssetManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.restarea_order.databinding.ActivityCsvBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.opencsv.CSVReader
import com.opencsv.exceptions.CsvException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class CSVActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCsvBinding
    private lateinit var mStore: FirebaseFirestore
    private lateinit var contentList: ArrayList<List<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCsvBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mStore = FirebaseFirestore.getInstance()
        try{
            loadData()
        }catch (e: IOException){
            e.printStackTrace()
        }catch (e: CsvException){
            e.printStackTrace()
        }

        binding.saveButton.setOnClickListener{
            saveData()
            Toast.makeText(this@CSVActivity, "저장되었습니다", Toast.LENGTH_SHORT).show()
        }
    }
    private fun loadData(){
        val assetManager: AssetManager = assets
        val inputStream: InputStream = assetManager.open("menu.csv")
        val csvReader = CSVReader(InputStreamReader(inputStream, "EUC-KR"))

        val allContent = csvReader.readAll()
        contentList = ArrayList()

        for (content in allContent){
            val rowData: List<String> = content.toList()
            contentList.add(rowData)
        }
        binding.csvReader.text = contentList.toString()
    }
    private fun saveData(){
        for (i in contentList.indices){
            val collectionName = contentList[i][0] //휴게소
            val documentName = contentList[i][1] //상품이름(메뉴)

            val data = hashMapOf(
                "menu" to contentList[i][1], //상품이름(메뉴)
                "price" to contentList[i][2], //가격
                "category" to contentList[i][3], //카테고리(한식/중식/)
                "data" to contentList[i][4] //한줄 정보
            )

            val collectionReference: CollectionReference = mStore.collection(collectionName)
            val documentReference: DocumentReference = collectionReference.document(documentName)

            documentReference.get().addOnCompleteListener{task ->
                if (task.isSuccessful){
                    if (task.result?.exists() == true){
                        documentReference.update(data as Map<String, Any>)
                    }else{
                        documentReference.set(data)
                    }
                }else{
                    documentReference.set(data)
                }
                Toast.makeText(this@CSVActivity, "csv됨", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}