package com.example.lidobalneare

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lidobalneare.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*//setto parte centrale home
        DBMSboundary().getDataHomePage(applicationContext, object : QueryReturnCallback<List<ViewModelHomePage>> {
            override fun onReturnValue(response: List<ViewModelHomePage>, message: String) {
                binding.recyclerview.adapter = MyAdapterHomePage(response)
            }

            override fun onQueryFailed(fail: String) {
                // Gestisci il caso in cui la query non abbia avuto successo
                Toast.makeText(applicationContext, fail, Toast.LENGTH_SHORT).show()
            }

            override fun onQueryError(error: String) {
                // Gestisci l'errore nella query
                Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
            }
        })*/


        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        /*binding.recyclerview.adapter = MyAdapterHomePage(
            listOf(
                ViewModelHomePage(listOf(), "ciao", "prova1"),
                ViewModelHomePage(listOf(), "ciao2", "prova1"),
                ViewModelHomePage(listOf(), "ciao3", "prova1"),
                ViewModelHomePage(listOf(), "ciao4", "prova1")
            )
        )*/

        //setto parte superiore home
        binding.viewPager.adapter =
            ImageHomeAdapter(listOf(R.drawable.prova_welcome, R.drawable.image_promo))
        binding.dotsIndicator.attachTo(binding.viewPager)

        //setto parte inferiore home
        binding.navigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.buttonPrenota -> {
                    Log.i("msg", "prenota")
                    val i = Intent(this, MainPrenotazione::class.java)
                    startActivity(i)
                    true
                }

                else -> false
            }

        }
    }

}
