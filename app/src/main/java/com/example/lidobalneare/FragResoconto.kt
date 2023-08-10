package com.example.lidobalneare

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.type.Money
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * str contiene una serie di stringhe, devono essere 3, esse
 * rappresentano Arrivo, Partenza e Hai selezionato della view frag_resoconto
 *
 * strEuro contiene i
 */
class FragResoconto(private val viewModelHomePage: ViewModelHomePage, private val prezzo: Money, private val sconto: Double): Fragment(R.layout.fragment_resoconto) {

    //percentuale di sconto del 5% fissa
    private val SCONTO_PAGA_IN_ANTICIPO = 0.05
    //iva al 22%
    private val IVA = 0.22
    //tasse varie 3%
    private val TASSE = 0.03

    //sconto fatto dal proprietario
    private fun getSconto(): Double{
        return if(sconto < 0 || sconto > 1){
            0.0
        }else{
            sconto
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //gestire la view

        if(viewModelHomePage.hasData()){
            view.findViewById<ImageView>(R.id.imageResoconto).setImageDrawable(viewModelHomePage.image)
            view.findViewById<TextView>(R.id.titleCamera).text = viewModelHomePage.title
            view.findViewById<TextView>(R.id.descLocazione).text = viewModelHomePage.desc
        }

        //calcolo vari costi
        view.findViewById<TextView>(R.id.textPrezzoOriginale).text = getString(R.string.euro_variabile, "${prezzo.units}.${prezzo.nanos}")

        val scontoMobile = if(view.findViewById<CheckBox>(R.id.checkBoxPagaInAnticipo).isChecked){
            applicaSconto(SCONTO_PAGA_IN_ANTICIPO)
        }else{
            "0"
        }

        view.findViewById<TextView>(R.id.textScontoPagaInAnticipo).text = getString(R.string.euro_variabile_sconto, scontoMobile)

        val sconto = applicaSconto(getSconto())
        view.findViewById<TextView>(R.id.textSconto).text = getString(R.string.euro_variabile_sconto, sconto)
        val iva = applicaSconto(IVA)
        view.findViewById<TextView>(R.id.textIva).text = getString(R.string.euro_variabile, iva)
        val tasse = applicaSconto(TASSE)
        view.findViewById<TextView>(R.id.textTasse).text = getString(R.string.euro_variabile, tasse)

        val totale = somma("${prezzo.units}.${prezzo.nanos}", scontoMobile, sconto, iva, tasse)
        view.findViewById<TextView>(R.id.textTotale).text = getString(R.string.euro_variabile, totale)

        val bundle = Bundle()
        bundle.putString("totale", totale)
        parentFragmentManager.setFragmentResult("costoTotale", bundle)

    }

    private fun applicaSconto(amount: Double): String{
        val originalAmount = BigDecimal("${prezzo.units}.${prezzo.nanos}")
        return  originalAmount.multiply(BigDecimal(amount)).setScale(2, RoundingMode.HALF_UP).toString()
    }

    /**
     * restituisce una stringa che rappresenta un
     * valore moneratorio come 10.50 in EUR
     */
    private fun somma(vararg num: String): String{
        var sum = BigDecimal(0)
        for (i in num){
            sum = sum.add(BigDecimal(i))
        }
        return sum.setScale(2, RoundingMode.HALF_UP).toString()
    }


}