package com.example.restarea_order

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.restarea_order.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user != null) {
            val uid = user.uid
            Log.d("uid",uid)
            binding!!.userid.text = uid
        }
            binding!!.csvButton.setOnClickListener{
                val intent = Intent(requireContext(), CSVActivity::class.java)
                startActivity(intent)
            }


        return binding!!.root
    }


}