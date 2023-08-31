package com.example.lidobalneare

import android.util.Log
import com.google.type.Money
import java.time.LocalDate

class CartaPrepagata(private val numeroCarta: String, private val cvv: String, private val dataScadenza: LocalDate) {

    fun getNumeroCarta(): String{
        return numeroCarta
    }

    fun getCvv(): String{
        return cvv
    }

    fun getDataScadeza(): LocalDate{
        return dataScadenza
    }

    //setto saldo iniziale di 3000€
    private var saldo: MyMoney = MyMoney(Money.newBuilder().setCurrencyCode("EUR").setUnits(3000).build())

    fun getSaldo(): MyMoney {
        return saldo
    }

    fun deposita(importo: Money) {
        saldo = saldo.sum(importo)
    }

    fun preleva(importo: MyMoney) {
        if (saldo.compareTo(importo) > 1) {
            saldo = saldo.sum(importo.get())
        } else {
            throw IllegalStateException("Saldo insufficiente per effettuare il prelievo.")
        }
    }

    /**
     * effettua il pagamento todo registrandolo da qualche parte
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

            // todo Aggiungi qui la logica per registrare il pagamento effettuato
        } else {
            throw IllegalStateException("Saldo insufficiente per effettuare il pagamento.")
        }

        return false
    }

    override fun toString(): String {
        return "carta: $numeroCarta $cvv $dataScadenza\n"
    }

}