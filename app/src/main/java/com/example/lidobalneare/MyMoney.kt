package com.example.lidobalneare

import com.google.type.Money
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.sign

class MyMoney(m: Money) {
    /**
     * inizializzata a 0 EUR
     */
    private var money: Money = m

        //todo testare
        fun sum(m1: Money, vararg m: Money?): MyMoney{
            lateinit var moneyOut: BigDecimal

            if(m.isNotEmpty()){
                val listMoney = ArrayList<String>()
                for(i in m){
                    if (i != null) {
                        listMoney.add("${i.units}.${i.nanos}")
                    }
                }
                moneyOut = somma(*listMoney.toTypedArray())
            }else{
                if(m1.currencyCode.equals(money.currencyCode)){
                    moneyOut = somma("${m1.units}.${m1.nanos}", "${money.units}.${money.nanos}")
                }
            }

            return MyMoney(Money.newBuilder()
                .setCurrencyCode("EUR")
                .setUnits(moneyOut.toBigInteger().toLong()).setNanos(
                    moneyOut.remainder(BigDecimal.ONE).multiply(BigDecimal(1000)).toInt()).build())

        }

        private fun somma(vararg num: String): BigDecimal{
            var sum = BigDecimal(0)
            for (i in num){
                sum = sum.add(BigDecimal(i))
            }
            return sum.setScale(2, RoundingMode.HALF_UP)
        }


    constructor(m: BigDecimal): this(Money.newBuilder().setNanos(m.remainder(BigDecimal.ONE).multiply(BigDecimal(1000)).toInt()).setUnits(m.toBigInteger().toLong()).setCurrencyCode("EUR").build())

    constructor(): this(Money.newBuilder().setNanos(0).setUnits(0).setCurrencyCode("EUR").build())

    /**
     * setta una nuova moneta con il suo valore anche
     * se gia c'era, quindi la va a sovrascrivere
     */
    fun set(units: Long, nanos: Int){
        money = Money.newBuilder().setNanos(nanos).setUnits(units).setCurrencyCode("EUR").build()
    }

    fun get(): Money{
        return money
    }

    /**
    * se l'oggetto che chiama è maggiore ritorna 1
    * se minore -1
    * se uguale 0
    */
    fun compareTo(m: MyMoney): Int{
        return if(m.money.units > this.money.units){
            -1
        }else if(m.money.units < this.money.units){
            1
        }else{
            if(m.money.nanos > this.money.nanos){
                -1
            }else if(m.money.nanos < this.money.nanos){
                1
            }
            else{
                0
            }
        }
    }

    /**
     * cambia il segno da positivo a negativo e viceversa
     * @return un nuovo oggetto MyMoney con il segno cambiato
     */
    fun changeSigne(): MyMoney{
        return MyMoney(Money.newBuilder(money).setUnits(-money.units).build())
    }

    /**
     * se è positivo torna 1
     * se è negativo torna -1
     * se è zero torna 0
     */
    fun getSigne(): Int{
        return money.units.sign
    }

    override fun toString(): String{
       return money.toString()
    }

    fun toMoney(): Money{
        return money
    }













}