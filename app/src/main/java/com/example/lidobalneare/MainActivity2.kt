package com.example.lidobalneare

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lidobalneare.databinding.ActivityMain2Binding

/**
 * main per lista prenotazioni
 */
class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerviewPrenotazioni.layoutManager = LinearLayoutManager(applicationContext)

        //recupero dati prenotazioni
        DBMSboundary().getPrenotazioni(applicationContext, object : QueryReturnCallback<List<ModelPrenotazione>>{
            override fun onReturnValue(response: List<ModelPrenotazione>, message: String) {
                if(response.isNotEmpty()){
                    binding.recyclerviewPrenotazioni.adapter = AdapterListPrenotazioni(applicationContext, response)
                }
            }

            override fun onQueryFailed(fail: String) {
                Toast.makeText(applicationContext, fail, Toast.LENGTH_SHORT).show()
            }

            override fun onQueryError(error: String) {
                Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
            }
        }, Utente.getInstance().getId())

        //setto parte inferiore home
        binding.navigationBarPrenotazioni.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.profilo -> {
                    val i = Intent(this, MainProfilo::class.java)
                    startActivity(i)
                    true
                }

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