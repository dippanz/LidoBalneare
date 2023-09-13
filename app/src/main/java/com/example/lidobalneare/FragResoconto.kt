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
 * @param viewModelHomePage l'immagine di resoconto con descrizione
 * @param prezzo rappresenta il prezzo di partenza
 * @param sconto rappresenta lo sconto fatto dalla struttura
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
        return if(sconto <= 0 || sconto > 1){
            0.0
        }else{
            sconto
        }
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //gestire la view

        if(viewModelHomePage.hasData()){
            view.findViewById<ImageView>(R.id.imageResoconto).setImageBitmap(viewModelHomePage.getImageBitmap())
            view.findViewById<TextView>(R.id.titleCamera).text = viewModelHomePage.title
            view.findViewById<TextView>(R.id.descLocazione).text = viewModelHomePage.desc
        }

        //calcolo vari costi
        view.findViewById<TextView>(R.id.textPrezzoOriginale).text = getString(R.string.euro_variabile,  BigDecimal("${prezzo.units}.${prezzo.nanos}").setScale(2, RoundingMode.HALF_UP).toString())

        val scontoMobile = if(view.findViewById<CheckBox>(R.id.checkBoxPagaInAnticipo).isChecked){
            applicaSconto(SCONTO_PAGA_IN_ANTICIPO, true)
        }else{
            "0"
        }

        view.findViewById<TextView>(R.id.textScontoPagaInAnticipo).text = getString(R.string.euro_variabile_sconto, scontoMobile)

        val sconto = applicaSconto(getSconto(), true)
        view.findViewById<TextView>(R.id.textSconto).text = getString(R.string.euro_variabile_sconto, sconto)
        val iva = applicaSconto(IVA)
        view.findViewById<TextView>(R.id.textIva).text = getString(R.string.euro_variabile, iva)
        val tasse = applicaSconto(TASSE)
        view.findViewById<TextView>(R.id.textTasse).text = getString(R.string.euro_variabile, tasse)

        val totale = somma("${prezzo.units}.${prezzo.nanos}", scontoMobile, sconto, iva, tasse)
        val viewTotale = view.findViewById<TextView>(R.id.textTotale)
        viewTotale.text = getString(R.string.euro_variabile, totale)

        val bundle = Bundle()
        bundle.putString("totale", totale)
        parentFragmentManager.setFragmentResult("costoTotale", bundle)


        view.findViewById<CheckBox>(R.id.checkBoxPagaInAnticipo).setOnCheckedChangeListener { _, b ->
            if(b){
                val scontoPagaAnticipo = applicaSconto(SCONTO_PAGA_IN_ANTICIPO, true)
                view.findViewById<TextView>(R.id.textScontoPagaInAnticipo).text = resources.getString(R.string.euro_variabile_sconto,scontoPagaAnticipo)
                viewTotale.text = resources.getString(R.string.euro_variabile, somma(totale, scontoPagaAnticipo))
            }else{
                view.findViewById<TextView>(R.id.textScontoPagaInAnticipo).text = resources.getString(R.string.euro_variabile_sconto, "0")
                viewTotale.text = resources.getString(R.string.euro_variabile, totale)
            }
        }

    }

    private fun applicaSconto(amount: Double, returnNegativeValue: Boolean = false): String{
        val originalAmount = BigDecimal("${prezzo.units}.${prezzo.nanos}")
        val result = originalAmount.multiply(BigDecimal(amount)).setScale(2, RoundingMode.HALF_UP)

        return if (returnNegativeValue) {
            result.negate().toString()
        } else {
            result.toString()
        }
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