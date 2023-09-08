package com.example.restarea_order.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.restarea_order.*
import com.example.restarea_order.Fragment.HistoryFragment
import com.example.restarea_order.Fragment.HomeFragment
import com.example.restarea_order.Fragment.InfoFragment
import com.example.restarea_order.Fragment.ProductFragment
import com.example.restarea_order.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        navigationItemSelect()
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.cart ->{
                val intent = Intent(this, AddActivity::class.java)
                startActivity(intent)
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun navigationItemSelect() {
        binding.navigation.run {
            setOnItemSelectedListener { item ->
                when(item.itemId) {
                    R.id.home -> {replaceFragment(HomeFragment())
                        binding.toolbar.setTitle("홈")
                    }
                    R.id.product -> {replaceFragment(ProductFragment())
                        binding.toolbar.setTitle("상품선택")
                       }
                    R.id.history ->{replaceFragment(HistoryFragment())
                        binding.toolbar.setTitle("주문내역")}
                    R.id.info ->{replaceFragment(InfoFragment())
                        binding.toolbar.setTitle("내 정보")}

                }
                true
            }
            selectedItemId = R.id.home
        }
    }
}