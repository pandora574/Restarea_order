package com.example.restarea_order.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.restarea_order.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
// Firebase를 초기화합니다.
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        binding.ButtonLogin.setOnClickListener {
            if (binding.EditTextId.text.toString()
                    .isNotEmpty() && binding.EditTextPw.text.toString().isNotEmpty()
            ) {
                auth.signInWithEmailAndPassword(
                    binding.EditTextId.text.toString(),
                    binding.EditTextPw.text.toString()
                ) //text중요 텍스트값을 가져옴 아니면 인스턴스값을 문자열로 가져옴
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
            }else{
                Toast.makeText(this, "값을 입력해주세요ㅅ", Toast.LENGTH_SHORT).show()
            }
        }
        binding.ButtonSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
    public override fun onStart() {
        super.onStart()
        moveMainPage(auth?.currentUser)
    }
    fun moveMainPage(user: FirebaseUser?){
        if( user!= null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }
    }
}