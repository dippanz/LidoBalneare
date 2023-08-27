package com.example.lidobalneare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lidobalneare.databinding.ActivityMainSchermateVuoteBinding

class MainActivitySchermateVuote : AppCompatActivity() {

    private lateinit var binding: ActivityMainSchermateVuoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainSchermateVuoteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val manager = supportFragmentManager.beginTransaction()
        when(intent.getIntExtra("layout", 0)){
            R.layout.pagamento_succes -> {
                val frag = FragPagamentSucces()
                val bundle = Bundle()
                bundle.putString("valueText", intent.getStringExtra("valueText"))
                frag.arguments = bundle
                manager.replace(R.id.fragment_container_schermate_vuote, frag)
            }

            R.layout.frag_loggin -> {
                manager.replace(R.id.fragment_container_schermate_vuote, FragLoggin())
            }

            R.layout.frag_mod_pagamento -> {
                val frag = FragModPagamento()
                frag.arguments = intent.getBundleExtra("fragResData")
                manager.replace(R.id.fragment_container_schermate_vuote, frag)
            }

            R.layout.frag_recensioni -> {
                val frag = FragRecensioni()
                val bundle = Bundle()
                bundle.putSerializable("response", intent.getSerializableExtra("response"))
                frag.arguments = bundle
                manager.replace(R.id.fragment_container_schermate_vuote, frag)
            }
        }
        manager.commit()


    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}