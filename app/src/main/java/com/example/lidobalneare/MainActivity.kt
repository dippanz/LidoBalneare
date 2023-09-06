package com.example.lidobalneare

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
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
        val adapter: MyAdapterHomePage

        if(Utente.getInstance().haveImageHome()){
            adapter = MyAdapterHomePage(Utente.getInstance().getImageHome())
        }else{
            adapter = MyAdapterHomePage(mutableListOf())

            DBMSboundary().getImageHome(applicationContext, object : QueryReturnCallback<ViewModelHomePage>{
                override fun onReturnValue(response: ViewModelHomePage, message: String) {
                    adapter.addViewModel(response)
                    Utente.getInstance().addImageHome(response)
                }

                override fun onQueryFailed(fail: String) {
                    Toast.makeText(applicationContext, fail, Toast.LENGTH_SHORT).show()
                }

                override fun onQueryError(error: String) {
                    Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                }
            })
        }




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

        val imageWelcome =
            ResourcesCompat.getDrawable(applicationContext.resources, R.drawable.prova_welcome, null)
                ?.toBitmap() as Bitmap
        val adapterViewPager: ImageHomeAdapter

        if(!Utente.getInstance().haveImagePromo()){

            adapterViewPager = ImageHomeAdapter(mutableListOf(imageWelcome))
            Utente.getInstance().addImagePromo(imageWelcome)

            DBMSboundary().getImagePromo(applicationContext, object : QueryReturnCallback<Bitmap>{
                override fun onReturnValue(response: Bitmap, message: String) {
                    adapterViewPager.addImage(response)
                    Utente.getInstance().addImagePromo(response)
                }

                override fun onQueryFailed(fail: String) {
                    Toast.makeText(applicationContext, fail, Toast.LENGTH_SHORT).show()
                }

                override fun onQueryError(error: String) {
                    Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                }
            }, listOf("promo1.png", "promo2.png"))
        }else{
            adapterViewPager = ImageHomeAdapter(Utente.getInstance().getImagePromo() as MutableList<Bitmap>)
        }





        binding.viewPager.adapter = adapterViewPager
        binding.dotsIndicator.attachTo(binding.viewPager)



        //setto parte inferiore home
        binding.navigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.buttonProfilo -> {
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
