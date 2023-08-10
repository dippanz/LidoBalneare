package com.example.lidobalneare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lidobalneare.databinding.ActivityMainAreaPersonaleBinding

class MainAreaPersonale : AppCompatActivity() {

    private lateinit var binding: ActivityMainAreaPersonaleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAreaPersonaleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val frag = intent.extras?.getString("frag")


        val manager = supportFragmentManager.beginTransaction()
        if(frag.equals("impostazioni")){
            //carico frag impostazioni
            manager.replace(R.id.containerAreaPersonale, FragImpostazioni())

        }else if(frag.equals("lemierecensioni")){
            //carico frag recensioni
            manager.replace(R.id.containerAreaPersonale, FragRecensioni())
        }
        manager.commit()



    }
}