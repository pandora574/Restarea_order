package com.example.restarea_order

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.restarea_order.databinding.FragmentInfoBinding
import com.google.firebase.auth.FirebaseAuth


class InfoFragment : Fragment() {

    private var binding:FragmentInfoBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        binding!!.ButtonLogout.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            auth.signOut()
        }

        return binding!!.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}