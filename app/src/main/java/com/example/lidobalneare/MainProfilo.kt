package com.example.lidobalneare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lidobalneare.databinding.ActivityMainProfiloBinding

class MainProfilo : AppCompatActivity() {

    private lateinit var binding: ActivityMainProfiloBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainProfiloBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manager = supportFragmentManager.beginTransaction()
        if(Utente.getInstance().isLoggedIn()){
            manager.replace(R.id.containerProfilo, FragProfilo())
        }else{
            //se non è stato effettuato l'accesso allora lo faccio
            manager.replace(R.id.containerProfilo, FragLoggin())
        }
        manager.commit()

        //setto parte inferiore profilo
        binding.navigationBarProfilo.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeBottomMenu -> {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }

        }




    }
}