package com.example.lidobalneare

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lidobalneare.databinding.ActivityMainBinding
import java.io.Serializable


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setto parte centrale home
        val adapter = MyAdapterHomePage(arrayListOf(
            ViewModelHomePage(AppCompatResources.getDrawable(this, R.drawable.barca), applicationContext.getString(R.string.title_barca), applicationContext.getString(R.string.desc_barca)),
            ViewModelHomePage(AppCompatResources.getDrawable(this, R.drawable.barcagiro), applicationContext.getString(R.string.title_giro_barca), applicationContext.getString(R.string.desc_giro_barca)),
            ViewModelHomePage(AppCompatResources.getDrawable(this, R.drawable.lettini_ombrelloni), applicationContext.getString(R.string.title_lettini), applicationContext.getString(R.string.desc_lettini)),
            ViewModelHomePage(AppCompatResources.getDrawable(this, R.drawable.divani_prive), applicationContext.getString(R.string.title_divanetti), applicationContext.getString(R.string.desc_divanetti)),
            ViewModelHomePage(AppCompatResources.getDrawable(this, R.drawable.motodacqua), applicationContext.getString(R.string.title_moto_acqua), applicationContext.getString(R.string.desc_moto_acqua)),
            ViewModelHomePage(AppCompatResources.getDrawable(this, R.drawable.pedalo), applicationContext.getString(R.string.title_pedalo), applicationContext.getString(R.string.desc_pedalo)),
            ViewModelHomePage(AppCompatResources.getDrawable(this, R.drawable.pingpong), applicationContext.getString(R.string.title_pingpong), applicationContext.getString(R.string.desc_pingpong)),
        )
        )

        adapter.setOnClickListener(object : MyAdapterHomePage.OnClickListener {
            override fun onClick(position: Int, model: ViewModelHomePage) {
                val intent = Intent(applicationContext, MainPrenotazione::class.java)

                //l'ordine è fissato
                when(position){
                    0 -> {
                        intent.putExtra("nome", "barca")
                        intent.putExtra("spunte", true)
                    }
                    1 ->{
                        intent.putExtra("nome", "barcagiro")
                        intent.putExtra("spunte", true)
                    }
                    2 ->{
                        intent.putExtra("nome", "lettini")
                    }
                    3 ->{
                        intent.putExtra("nome", "motoacqua")
                        intent.putExtra("spunte", true)
                    }
                    4 ->{
                        intent.putExtra("nome", "pedalo")
                        intent.putExtra("spunte", true)
                    }
                    5 ->{
                        intent.putExtra("nome", "pingpong")
                        intent.putExtra("spunte", true)
                    }
                    6 ->{
                        intent.putExtra("nome", "prive")
                }
                    else -> return
                }

                intent.putExtra("cardview", model as Serializable)
                // Avvia l'activity desiderata
                startActivity(intent)
            }
        })

        binding.recyclerview.adapter = adapter

        binding.recyclerview.layoutManager = LinearLayoutManager(this)


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
