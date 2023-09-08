package com.example.restarea_order.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.restarea_order.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    val db_store = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.ButtonSignup.setOnClickListener {
            val email = binding.EditTextId.text.toString()
            val password = binding.EditTextPw.text.toString()
            val confirm_password = binding.EditTextConfirmpPw.text.toString()
            val name = binding.EditTextName.text.toString()
            val user_num = binding.EditTextUsernum.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty() && password==confirm_password){
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser
                        if (user != null) {
                            val uid = user.uid

                            val userinfo = hashMapOf(
                                "username" to  name,
                                "email" to email,
                                "password" to password,
                                "uid" to uid,
                                "usernum" to user_num
                            )
                            db_store.collection("account").document(uid).set(userinfo)
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                        Log.d("auth",user.toString())


                    }else{
                        Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }else{
                Toast.makeText(this, "입력정보를 확인하세요", Toast.LENGTH_SHORT).show()
        }


        }
        binding.ButtonLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}