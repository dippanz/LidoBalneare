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
import java.io.Serializable
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Month
import java.time.temporal.ChronoUnit
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
                        binding.parentRecensioni.setBackgroundColor(resources.getColor(R.color.white))

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
                        binding.parentRecensioni.setBackgroundColor(resources.getColor(R.color.grigio_chiaro))

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

            //prezzo originale preso all'inizio
            var prezzoTmp: BigDecimal? = null

            var prezzoPersoneTmp: BigDecimal? = null
            //prezzo che varia al variare delle persone che metto
            var prezzoDatetmp: BigDecimal? = null

            //setto listener per capire se impostare moltiplicatore prezzo
            binding.textNumPersone.addTextChangedListener(object : TextWatcher {

                var beforeChange = 0

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    if(prezzoTmp == null){
                        prezzoTmp = BigDecimal(
                            supportFragmentManager.fragments[0].requireActivity().
                            findViewById<TextView>(R.id.textPrezzoOriginale).text.toString().split(" ")[1])
                    }

                    val (startDate, endDate) = parseDateRange(binding.textDate.text.toString())
                    if(prezzoPersoneTmp == null){
                        prezzoPersoneTmp = prezzoTmp!!.multiply(BigDecimal(ChronoUnit.DAYS.between(startDate, endDate) + 1))
                    }

                    prezzoDatetmp = null
                    beforeChange = s.toString().split(" ")[0].toInt()
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Invocato durante la modifica del testo
                }

                override fun afterTextChanged(s: Editable?) {
                    //ricarico il fragResoconto con il nuovo moltiplicatore
                    val prezzoAttuale = BigDecimal(
                        supportFragmentManager.fragments[0].requireActivity().
                        findViewById<TextView>(R.id.textPrezzoOriginale).text.toString().split(" ")[1])
                    val numAttuale = s.toString().split(" ")[0].toInt()
                    val prezzo =
                    if(beforeChange < numAttuale){
                        val increase = numAttuale - beforeChange
                        MyMoney(
                            prezzoAttuale.add(
                                prezzoPersoneTmp?.multiply(
                                    BigDecimal(increase)) ?: BigDecimal(1)
                        ))

                    }else  if(beforeChange > numAttuale){
                        val decrease = beforeChange - numAttuale
                        MyMoney(
                            prezzoAttuale.subtract(
                                prezzoPersoneTmp?.multiply(
                                    BigDecimal(decrease)) ?: BigDecimal(1)
                            ))
                    }else{
                        MyMoney(prezzoAttuale)
                    }

                    val t = manager.beginTransaction()
                    viewModelCaricato = (intent.getSerializableExtra("cardview") as ViewModelHomePage)
                    t.replace(R.id.fragment_container_resoconto, FragResoconto(viewModelCaricato, prezzo.toMoney(), 0.1))
                    t.commit()

                }
            })

            binding.textDate.addTextChangedListener(object : TextWatcher {

                var rangeBeforeChange = 0L

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    if(prezzoTmp == null){
                        prezzoTmp = BigDecimal(
                            supportFragmentManager.fragments[0].requireActivity().
                            findViewById<TextView>(R.id.textPrezzoOriginale).text.toString().split(" ")[1])
                    }

                    if(prezzoDatetmp == null){

                        prezzoDatetmp = prezzoTmp?.multiply(BigDecimal(
                            supportFragmentManager.fragments[0].requireActivity().
                            findViewById<TextView>(R.id.textNumPersone).text.toString().split(" ")[0]))
                    }

                    val (startDate, endDate) = parseDateRange(s.toString())
                    prezzoPersoneTmp = null
                    rangeBeforeChange = ChronoUnit.DAYS.between(startDate, endDate) + 1
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Invocato durante la modifica del testo
                }

                override fun afterTextChanged(s: Editable?) {
                    //ricarico il fragResoconto con il nuovo moltiplicatore
                    val prezzoAttuale = BigDecimal(
                        supportFragmentManager.fragments[0].requireActivity().
                        findViewById<TextView>(R.id.textPrezzoOriginale).text.toString().split(" ")[1])
                    val (startDate, endDate) = parseDateRange(s.toString())
                    val rangeAttuale = ChronoUnit.DAYS.between(startDate, endDate) + 1
                    val prezzo =
                        if(rangeBeforeChange < rangeAttuale){
                            val increase = rangeAttuale - rangeBeforeChange
                            MyMoney(
                                prezzoAttuale.add(
                                    prezzoDatetmp?.multiply(
                                        BigDecimal(increase)) ?: BigDecimal(1)
                                ))

                        }else  if(rangeBeforeChange > rangeAttuale){
                            val decrease = rangeBeforeChange - rangeAttuale
                            MyMoney(
                                prezzoAttuale.subtract(
                                    prezzoDatetmp?.multiply(
                                        BigDecimal(decrease)) ?: BigDecimal(1)
                                ))
                        }else{
                            MyMoney(prezzoAttuale)
                        }

                    val t = manager.beginTransaction()
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

        intent.getStringExtra("nome")?.let {nomeServizio: String ->
            DBMSboundary().getRecensioniServizio(applicationContext, object : QueryReturnCallback<List<ModelRecensioni>>{
                override fun onReturnValue(response: List<ModelRecensioni>, message: String) {
                    when(response.size){
                        0 -> {
                            binding.includedRecensioni.rec1.visibility = View.GONE
                            binding.includedRecensioni.rec2.visibility = View.GONE
                            binding.includedRecensioni.rec3.visibility = View.GONE
                        }
                        1 -> {
                            binding.includedRecensioni.rec1.visibility = View.VISIBLE
                            binding.includedRecensioni.titoloRecensione1.text = response[0].titolo
                            binding.includedRecensioni.ratingBarRecensioniFatte1.rating = response[0].valutazione
                        }

                        2 -> {
                            binding.includedRecensioni.rec1.visibility = View.VISIBLE
                            binding.includedRecensioni.titoloRecensione1.text = response[0].titolo
                            binding.includedRecensioni.ratingBarRecensioniFatte1.rating = response[0].valutazione

                            binding.includedRecensioni.rec2.visibility = View.VISIBLE
                            binding.includedRecensioni.titoloRecensione2.text = response[1].titolo
                            binding.includedRecensioni.ratingBarRecensioniFatte2.rating = response[1].valutazione



                        }

                        else -> {
                            binding.includedRecensioni.rec1.visibility = View.VISIBLE
                            binding.includedRecensioni.titoloRecensione1.text = response[0].titolo
                            binding.includedRecensioni.ratingBarRecensioniFatte1.rating = response[0].valutazione

                            binding.includedRecensioni.rec2.visibility = View.VISIBLE
                            binding.includedRecensioni.titoloRecensione2.text = response[1].titolo
                            binding.includedRecensioni.ratingBarRecensioniFatte2.rating = response[1].valutazione

                            binding.includedRecensioni.rec3.visibility = View.VISIBLE
                            binding.includedRecensioni.titoloRecensione3.text = response[2].titolo
                            binding.includedRecensioni.ratingBarRecensioniFatte3.rating = response[2].valutazione
                        }
                    }

                    binding.includedRecensioni.parent2Recensioni.setOnClickListener {
                        val i = Intent(applicationContext, MainActivitySchermateVuote::class.java)
                        i.putExtra("layout", R.layout.frag_recensioni)
                        i.putExtra("nomeServizio", nomeServizio)
                        i.putExtra("response", response as Serializable)
                        startActivity(i)
                    }
                }

                override fun onQueryFailed(fail: String) {
                    Toast.makeText(applicationContext, fail, Toast.LENGTH_SHORT).show()
                }

                override fun onQueryError(error: String) {
                    Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                }
            }, nomeServizio)
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