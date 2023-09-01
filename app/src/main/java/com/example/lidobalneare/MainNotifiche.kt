package com.example.lidobalneare

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lidobalneare.databinding.ActivityMainNotificheBinding

class MainNotifiche : AppCompatActivity() {

    private lateinit var binding: ActivityMainNotificheBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainNotificheBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerviewNotifiche.layoutManager = LinearLayoutManager(applicationContext)
        DBMSboundary().getNotifiche(applicationContext, object : QueryReturnCallback<List<ModelNotifica>>{
            override fun onReturnValue(response: List<ModelNotifica>, message: String) {
                if (response.isEmpty()){
                    binding.recyclerviewNotifiche.visibility = View.GONE
                    binding.includedNotifica.root.visibility = View.VISIBLE
                }else{
                    binding.recyclerviewNotifiche.adapter = AdapterNotifica(response.toMutableList())
                }

            }

            override fun onQueryFailed(fail: String) {
                Toast.makeText(applicationContext, fail, Toast.LENGTH_SHORT).show()
            }

            override fun onQueryError(error: String) {
                Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()

            }
        }, Utente.getInstance().getId())

        //setto parte inferiore profilo
        binding.navigationBarNotifiche.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeBottomMenu -> {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.buttonPrenotazioni -> {
                    if(Utente.getInstance().isLoggedIn()){
                        val i = Intent(this, MainActivity2::class.java)
                        finish()
                        startActivity(i)
                    }
                    true
                }

                R.id.buttonProfilo -> {
                    if(Utente.getInstance().isLoggedIn()){
                        val i = Intent(this, MainProfilo::class.java)
                        finish()
                        startActivity(i)
                    }
                    true
                }


                else -> false
            }

        }
    }
}