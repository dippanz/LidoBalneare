package com.example.lidobalneare

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




    }
}