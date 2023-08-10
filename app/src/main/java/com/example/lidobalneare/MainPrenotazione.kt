package com.example.lidobalneare

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lidobalneare.databinding.ActivityMainPrenotazioneBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior


class MainPrenotazione : AppCompatActivity() {

    private lateinit var binding: ActivityMainPrenotazioneBinding
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPrenotazioneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manager = supportFragmentManager
        mBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        binding.imageButtonCalendar.setOnClickListener{
            if(manager.backStackEntryCount == 0){
                //carico il fragment se non c'era gia
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.fragmentView, CalendarFragment())
                transaction.addToBackStack(null)
                transaction.commit()

                //Eseguo il commit in modo sincrono
                manager.executePendingTransactions()

                //espando il bottom sheet
                mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        binding.imageButtonPerson.setOnClickListener {
            if(manager.backStackEntryCount == 0) {
                //carico il fragment
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.fragmentView, NumPersonFragment())
                transaction.addToBackStack(null)
                transaction.commit()

                //Eseguo il commit in modo sincrono
                manager.executePendingTransactions()

                //espando il bottom sheet
                mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        mBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.buttonPrenota2.visibility = View.VISIBLE
                        if(manager.backStackEntryCount != 0){
                            manager.popBackStack()
                        }
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                        //disabilito visibilita e funzionamento del tasto
                        binding.buttonPrenota2.visibility = View.GONE
                    }
                    else -> Log.i("msg", "stato: $newState")

                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })



        //prelevo dati per caricare resoconto
        DBMSboundary().getPrezzoServizio(applicationContext, object : QueryReturnCallback<MyMoney>{
            override fun onReturnValue(response: MyMoney, message: String) {

                //inserisco resoconto  todo inserire dati da intent che apre activity dovrebbe essere dalle immagini della prima recycler view
                val t = manager.beginTransaction()
                if(intent.getBooleanExtra("spunte", false)){
                    t.replace(R.id.fragment_container_spunte, FragSpunte())
                }
                t.replace(R.id.fragment_container_resoconto, FragResoconto( intent.getSerializableExtra("cardview") as ViewModelHomePage, response.toMoney(), 0.1))
                t.commit()
            }

            override fun onQueryFailed(fail: String) {
                // Gestisci il caso in cui la query non abbia avuto successo
                Toast.makeText(applicationContext, fail, Toast.LENGTH_SHORT).show()
            }

            override fun onQueryError(error: String) {
                // Gestisci l'errore nella query
                Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
            }

        }, intent.getStringExtra("nome").toString())








        //setto comportamento bottone prenota
        binding.buttonPrenota2.setOnClickListener {

        }




    }



}