package com.example.lidobalneare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
            ViewModelHomePage(R.drawable.barca, applicationContext.getString(R.string.title_barca), applicationContext.getString(R.string.desc_barca)),
            ViewModelHomePage(R.drawable.barcagiro, applicationContext.getString(R.string.title_giro_barca), applicationContext.getString(R.string.desc_giro_barca)),
            ViewModelHomePage(R.drawable.lettini_ombrelloni, applicationContext.getString(R.string.title_lettini), applicationContext.getString(R.string.desc_lettini)),
            ViewModelHomePage(R.drawable.divani_prive, applicationContext.getString(R.string.title_divanetti), applicationContext.getString(R.string.desc_divanetti)),
            ViewModelHomePage(R.drawable.motodacqua, applicationContext.getString(R.string.title_moto_acqua), applicationContext.getString(R.string.desc_moto_acqua)),
            ViewModelHomePage(R.drawable.pedalo, applicationContext.getString(R.string.title_pedalo), applicationContext.getString(R.string.desc_pedalo)),
            ViewModelHomePage(R.drawable.pingpong, applicationContext.getString(R.string.title_pingpong), applicationContext.getString(R.string.desc_pingpong)),
        )
        )

        adapter.setOnClickListener(object : MyAdapterHomePage.OnClickListener {
            override fun onClick(position: Int, model: ViewModelHomePage) {
                if(Utente.getInstance().isLoggedIn()) {
                    val intent = Intent(applicationContext, MainPrenotazione::class.java)

                    //l'ordine è fissato
                    when (position) {
                        0 -> {
                            intent.putExtra("nome", "barca")
                            //serve per capire se serve il moltiplicatore per il prezzo
                            intent.putExtra("aTesta", true)
                        }

                        1 -> {
                            intent.putExtra("nome", "barcagiro")
                            intent.putExtra("aTesta", true)
                        }

                        2 -> {
                            intent.putExtra("nome", "lettini")
                            //questo extra serve a capire se mostrare o meno bottone del lettino
                            intent.putExtra("buttonLettino", true)
                        }

                        3 -> {
                            intent.putExtra("nome", "motoacqua")
                            intent.putExtra("aTesta", true)

                        }

                        4 -> {
                            intent.putExtra("nome", "pedalo")
                        }

                        5 -> {
                            intent.putExtra("nome", "pingpong")
                            intent.putExtra("aTesta", true)
                        }

                        6 -> {
                            intent.putExtra("nome", "prive")
                            intent.putExtra("buttonLettino", true)
                        }

                        else -> return
                    }

                    intent.putExtra("cardview", model as Serializable)
                    // Avvia l'activity desiderata
                    startActivity(intent)
                }else{
                    //se l'utente non logga non permetto di prenotare
                    val i = Intent(applicationContext, MainActivitySchermateVuote::class.java)
                    i.putExtra("layout", R.layout.frag_loggin)
                    startActivity(i)
                }
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
                R.id.profilo -> {
                    val i = Intent(this, MainProfilo::class.java)
                    startActivity(i)
                    true
                }

                R.id.buttonPrenotazioni -> {
                    if(Utente.getInstance().isLoggedIn()){
                        val i = Intent(this, MainActivity2::class.java)
                        startActivity(i)
                    }else{
                        val i = Intent(this, MainActivitySchermateVuote::class.java)
                        i.putExtra("layout", R.layout.frag_loggin)
                        startActivity(i)
                    }
                    true
                }

                R.id.notifiche ->{
                    if(Utente.getInstance().isLoggedIn()){
                        val i = Intent(this, MainNotifiche::class.java)
                        startActivity(i)
                    }else{
                        val i = Intent(this, MainActivitySchermateVuote::class.java)
                        i.putExtra("layout", R.layout.frag_loggin)
                        startActivity(i)
                    }
                    true
                }
                else -> false
            }

        }


    }


}
