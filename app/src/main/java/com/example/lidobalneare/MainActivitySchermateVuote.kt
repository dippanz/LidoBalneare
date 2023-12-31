package com.example.lidobalneare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lidobalneare.databinding.ActivityMainSchermateVuoteBinding

class MainActivitySchermateVuote : AppCompatActivity() {

    private lateinit var binding: ActivityMainSchermateVuoteBinding
    private var backHome: Boolean = false

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
                backHome = true
            }

            R.layout.frag_loggin -> {
                manager.replace(R.id.fragment_container_schermate_vuote, FragLoggin())
                backHome = true
            }

            R.layout.frag_mod_pagamento -> {
                val frag = FragModPagamento()
                frag.arguments = intent.getBundleExtra("fragResData")
                manager.replace(R.id.fragment_container_schermate_vuote, frag)
                backHome = false
            }

            R.layout.frag_recensioni -> {
                val frag = FragRecensioni()
                val bundle = Bundle()
                bundle.putSerializable("response", intent.getSerializableExtra("response"))
                bundle.putString("nomeServizio", intent.getStringExtra("nomeServizio"))
                frag.arguments = bundle
                manager.replace(R.id.fragment_container_schermate_vuote, frag)
                backHome = false
            }
        }
        manager.commit()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if(backHome ||
            supportFragmentManager.findFragmentById(binding.fragmentContainerSchermateVuote.id) == supportFragmentManager.findFragmentByTag("FragPagamentSucces")){
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        finish()
    }
}