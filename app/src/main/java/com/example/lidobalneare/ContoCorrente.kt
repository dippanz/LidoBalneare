package com.example.lidobalneare

import android.util.Log
import com.google.type.Money

class ContoCorrente(private val numeroConto: String, private val iban: String, private val nomeTitolare: String) {

    //setto saldo iniziale di 3000€
    private var saldo: MyMoney = MyMoney(Money.newBuilder().setCurrencyCode("EUR").setUnits(3000).build())

    fun getIban(): String{
        return iban
    }

    fun getNomeTitolare(): String{
        return nomeTitolare
    }

    /**
     * effettua il pagamento
     *  @param importo deve essere un valore positivo
     *
     *  @throws IllegalStateException se il saldo è insufficiente viene buttato un eccezzione da gestire
     */
    @Throws(IllegalStateException::class)
    fun effettuaPagamento(importo: MyMoney): Boolean {
        if (saldo.compareTo(importo) >= 1) {
            if(importo.getSigne() > 0){
                val importNeg = importo.changeSigne().get()
                saldo = saldo.sum(importNeg)
                return true
            }else{
                Log.i("msg", "problema con pagamento")
            }

        } else {
            throw IllegalStateException("Saldo insufficiente per effettuare il pagamento.")
        }

        return false
    }

    override fun toString(): String {
        return "Conto PayPal: $numeroConto $nomeTitolare $iban\n"
    }


}
