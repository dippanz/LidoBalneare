package com.example.lidobalneare

import com.google.type.Money

class PayPal(private val numeroConto: String, private val email: String, private val nomeTitolare: String, private var numTelefono: String) {

    //setto saldo iniziale di 3000€
    private var saldo: Money = Money.newBuilder().setCurrencyCode("EUR").setUnits(3000).build()

    fun getEmail(): String{
        return email
    }

    fun getNomeTitolare(): String{
        return nomeTitolare
    }
}
