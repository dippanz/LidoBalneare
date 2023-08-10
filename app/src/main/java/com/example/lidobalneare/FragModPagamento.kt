package com.example.lidobalneare

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.type.Money
import java.math.BigDecimal

class FragModPagamento: Fragment(R.layout.frag_mod_pagamento) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager = parentFragmentManager
        val transaction = manager.beginTransaction()

        transaction.add(
            R.id.frag_resoconto,
            FragResoconto(
                arguments?.getSerializable("viewModel") as ViewModelHomePage,
                Money.newBuilder().setCurrencyCode("EUR").setUnits(10).setNanos(50).build(),
                0.05
            )
        )
        transaction.commit()

        var importoMoney = MyMoney()

        parentFragmentManager.setFragmentResultListener("costoTotale", this) { requestKey, bundle ->

            val importoTotale = bundle.getString("totale")?.toBigDecimal()

            if (importoTotale != null) {
                importoMoney = MyMoney(
                    Money.newBuilder().setCurrencyCode("EUR").setUnits(
                        importoTotale.abs().toBigInteger().toLong()
                    ).setNanos(
                        importoTotale.abs().remainder(
                            BigDecimal.ONE
                        ).toInt()
                    ).build()
                )
            } else {
                Log.i("msgError", "conversione totale fallita")
            }

        }

        var radioButton = 0
        view.findViewById<RadioGroup>(R.id.radioGroup).setOnCheckedChangeListener { group, checkedId ->
            radioButton = when (checkedId) {
                R.id.radioButtonPayPal -> {
                    Log.i("msg", "1")
                    1
                }

                R.id.radioButtonCarta -> {
                    Log.i("msg", "2")
                    2
                }

                R.id.radioButtonBonifico -> {
                    Log.i("msg", "3")
                    3
                }

                else -> 0
            }
        }

        view.findViewById<Button>(R.id.buttonPaga).setOnClickListener {

                //TODO implementare metodo simulato per la gestione dei pagamenti
                if (radioButton == 1) {

                } else if (radioButton == 2) {


                    try {
                        //se il pagamento è stato effettuato con successo
                        if (Utente.getInstance().getCarta()?.effettuaPagamento(importoMoney) == true) {
                            //mostro il frag di avvenuto pagamento
                            val t = parentFragmentManager.beginTransaction()
                            t.replace(R.id.pagamento_view_frag, FragPagamentSucces())
                            t.commit()

                        } else {
                            Toast.makeText(requireContext(), "Il pagamento non è andato a buon fine!", Toast.LENGTH_LONG).show()
                            Toast.makeText(requireContext(), "Carta generata", Toast.LENGTH_SHORT).show()
                            Utente.getInstance().generateCarta()
                        }

                    } catch (e: IllegalStateException) {
                        e.printStackTrace()

                        //todo richiedere inserimento metodo di pagamento
                        Utente.getInstance().clearCarta().generateCarta()
                    }


                } else if (radioButton == 3) {

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Seleziona un metodo di pagamento",
                        Toast.LENGTH_LONG
                    ).show()
                }

        }
    }
}