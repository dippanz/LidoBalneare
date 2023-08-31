package com.example.lidobalneare

import com.google.type.Money

class ContoCorrente(private val numeroConto: String, private val iban: String, private val nomeTitolare: String) {

    //setto saldo iniziale di 3000€
    private var saldo: Money = Money.newBuilder().setCurrencyCode("EUR").setUnits(3000).build()

    fun getIban(): String{
        return iban
    }

    fun getNomeTitolare(): String{
        return nomeTitolare
    }


}
