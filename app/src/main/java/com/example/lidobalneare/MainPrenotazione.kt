package com.example.lidobalneare

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lidobalneare.databinding.ActivityMainPrenotazioneBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Month
import java.util.Calendar
import java.util.Locale


class MainPrenotazione : AppCompatActivity() {

    private lateinit var binding: ActivityMainPrenotazioneBinding
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var viewModelCaricato: ViewModelHomePage


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

        binding.imageButtonLettino.setOnClickListener {
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
                        binding.parentPrenotazione.setBackgroundColor(resources.getColor(R.color.white))
                        binding.imageButtonLettino.setBackgroundColor(resources.getColor(R.color.white))
                        binding.imageButtonPerson.setBackgroundColor(resources.getColor(R.color.white))
                        binding.imageButtonCalendar.setBackgroundColor(resources.getColor(R.color.white))

                        if(manager.backStackEntryCount != 0){
                            manager.popBackStack()
                        }
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                        //disabilito visibilita e funzionamento del tasto
                        binding.buttonPrenota2.visibility = View.GONE

                        //rendo il dietro scuro e non utilizzaile
                        binding.imageButtonLettino.setBackgroundColor(resources.getColor(R.color.trasparente))
                        binding.imageButtonPerson.setBackgroundColor(resources.getColor(R.color.trasparente))
                        binding.imageButtonCalendar.setBackgroundColor(resources.getColor(R.color.trasparente))
                        binding.parentPrenotazione.setBackgroundColor(resources.getColor(R.color.grigio_chiaro))

                    }
                    else -> Log.i("msg", "stato: $newState")

                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        //setto parte superiore
        // Formatta la data in "EEE dd MMM" (sab 12 ago)
        val dateFormat = SimpleDateFormat("EEE dd MMM", Locale.ITALIAN)
        val formattedDate: String = dateFormat.format(Calendar.getInstance().time)
        binding.textDate.text = formattedDate
        binding.textNumPersone.text = resources.getString(R.string.persone_1s, "1")
        binding.textNumLettini.text = resources.getString(R.string.lettini_1_s, "1")

        if(!intent.getBooleanExtra("buttonLettino", false)){
            //se non serve nascondo il bottone lettino
            binding.imageButtonLettino.visibility = View.GONE
            binding.textNumLettini.visibility = View.GONE
        }



        //prelevo dati per caricare resoconto
        intent.getStringExtra("nome")?.let {
            DBMSboundary().getPrezzoServizio(applicationContext, object : QueryReturnCallback<MyMoney>{
                override fun onReturnValue(response: MyMoney, message: String) {

                    //inserisco resoconto
                    val t = manager.beginTransaction()
                    t.replace(R.id.fragment_container_resoconto, FragResoconto(intent.getSerializableExtra("cardview") as ViewModelHomePage, response.toMoney(), 0.1))
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

            }, it)
        }

        if(intent.getBooleanExtra("aTesta", false)){
            //setto listener per capire se impostare moltiplicatore prezzo
            binding.textNumPersone.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Invocato prima che il testo venga modificato
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Invocato durante la modifica del testo
                }

                override fun afterTextChanged(s: Editable?) {
                    // Invocato dopo che il testo è stato modificato
                    val newText = s.toString()
                    //ricarico il fragResoconto con il nuovo moltiplicatore todo vedere performance
                    val t = manager.beginTransaction()

                    val prezzoString = supportFragmentManager.fragments[0].requireActivity().findViewById<TextView>(R.id.textPrezzoOriginale).text.toString()
                    val prezzoBigDecimal = prezzoString.substringAfter(" ").toBigDecimal()
                    Log.i("msg", prezzoBigDecimal.toString())

                    val prezzo = MyMoney(prezzoBigDecimal.multiply(BigDecimal(binding.textNumPersone.text.toString().substringBefore(" "))))
                    viewModelCaricato = (intent.getSerializableExtra("cardview") as ViewModelHomePage)
                    t.replace(R.id.fragment_container_resoconto, FragResoconto(viewModelCaricato, prezzo.toMoney(), 0.1))
                    t.commit()

                }
            })
        }




        //setto comportamento bottone prenota
        binding.buttonPrenota2.setOnClickListener {
            val (startDate, endDate) = parseDateRange(binding.textDate.text.toString())
            val numPerson = binding.textNumPersone.text.toString().split(" ")[0].toInt()


            


            if(binding.fragmentContainerResoconto.findViewById<CheckBox>(R.id.checkBoxPagaInAnticipo).isChecked){
                //se paga in anticipo allora mostro fragModPagamento
                val i = Intent(this, MainActivitySchermateVuote::class.java)
                i.putExtra("layout", R.layout.frag_mod_pagamento)
                val bundle = Bundle()

                val bundleQuery = Bundle()
                bundleQuery.putString("nomeServizio", intent.getStringExtra("nome"))
                bundleQuery.putSerializable("startDate", startDate)
                bundleQuery.putSerializable("endDate", endDate)
                bundleQuery.putInt("numPerson", numPerson)

                bundle.putBundle("bundleQuery", bundleQuery)
                bundle.putString("prezzoOriginale", binding.fragmentContainerResoconto.findViewById<TextView>(R.id.textPrezzoOriginale).text.toString().split(" ")[1])
                bundle.putSerializable("cardview", intent.getSerializableExtra("cardview"))
                i.putExtra("fragResData", bundle )
                startActivity(i)
            }else {
                //inserisco prenotazione
                intent.getStringExtra("nome")?.let { it1 ->
                    DBMSboundary().insertPrenotazione(applicationContext, false, startDate, endDate,
                        it1, Utente.getInstance().getId(), numPerson)
                }

                //mostro avvenuta prenotazione
                val i = Intent(this, MainActivitySchermateVuote::class.java)
                i.putExtra("layout", R.layout.pagamento_succes)
                i.putExtra("valueText", "Prenotazione avvenuta con successo!")
                finish()
                startActivity(i)
            }
        }
    }

    fun getBottomSheet(): BottomSheetBehavior<*> {
        return mBottomSheetBehavior
    }

    private fun parseDateRange(dateString: String): Pair<LocalDate, LocalDate> {
        val parts = dateString.split(" - ")
        val startDateString = parts[0]
        val endDateString = if (parts.size > 1) parts[1] else parts[0]

        val startDate = parseDateString(startDateString)
        val endDate = parseDateString(endDateString)

        return Pair(startDate, endDate)
    }

    private fun parseDateString(dateString: String): LocalDate {
        val monthMap = mapOf(
            "gen" to Month.JANUARY,
            "feb" to Month.FEBRUARY,
            "mar" to Month.MARCH,
            "apr" to Month.APRIL,
            "mag" to Month.MAY,
            "giu" to Month.JUNE,
            "lug" to Month.JULY,
            "ago" to Month.AUGUST,
            "set" to Month.SEPTEMBER,
            "ott" to Month.OCTOBER,
            "nov" to Month.NOVEMBER,
            "dic" to Month.DECEMBER
        )

        val parts = dateString.split(" ")
        val dayOfMonth = parts[1].toInt()
        val month = monthMap[parts[2]] ?: throw IllegalArgumentException("Month not recognized")
        val currentYear = LocalDate.now().year

        return LocalDate.of(currentYear, month, dayOfMonth)
    }




}