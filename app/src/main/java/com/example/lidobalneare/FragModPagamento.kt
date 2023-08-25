package com.example.lidobalneare

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.lidobalneare.databinding.FragModPagamentoBinding
import java.math.BigDecimal
import java.time.LocalDate

class FragModPagamento: Fragment(R.layout.frag_mod_pagamento) {

    private lateinit var binding: FragModPagamentoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragModPagamentoBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager = parentFragmentManager
        val transaction = manager.beginTransaction()
        val frag = FragResoconto(arguments?.getSerializable("cardview") as ViewModelHomePage,
            MyMoney(BigDecimal(requireArguments().getString("prezzoOriginale"))).toMoney(),
            0.0)
        transaction.replace(R.id.frag_resoconto_mod_pagamento, frag)
        transaction.commit()


        var radioButton = 0
        view.findViewById<RadioGroup>(R.id.radioGroup).setOnCheckedChangeListener { group, checkedId ->
            radioButton = when (checkedId) {
                R.id.radioButtonPayPal -> {
                    1
                }

                R.id.radioButtonCarta -> {
                    2
                }

                R.id.radioButtonBonifico -> {
                    3
                }

                else -> 0
            }
        }

        view.findViewById<Button>(R.id.buttonPaga).setOnClickListener {

            val importoTotale = binding.fragResocontoModPagamento.findViewById<TextView>(R.id.textTotale).text.toString().split(" ")[1]

            when (radioButton) {
                1 -> {
                    //paypal

                }

                2 -> {
                    try {
                        if (Utente.getInstance().getCarta()?.effettuaPagamento(MyMoney(BigDecimal(importoTotale))) == true) {
                           //inserisco prenotazione nel db
                            val bundleQuery = requireArguments().getBundle("bundleQuery")
                            val startDate = bundleQuery?.getSerializable("startDate") as LocalDate
                            val endDate = bundleQuery.getSerializable("endDate") as LocalDate
                            val numPerson = bundleQuery.getInt("numPerson")
                            val nomeServizio = bundleQuery.getString("nomeServizio") as String

                            DBMSboundary().insertPrenotazione(requireContext(), true, startDate, endDate,
                                nomeServizio, Utente.getInstance().getId(), numPerson)

                            Log.i("msg", "arrivo qua")

                            //se il pagamento è stato effettuato con successo
                            val t = parentFragmentManager.beginTransaction()
                            t.replace(R.id.fragment_container_schermate_vuote, FragPagamentSucces())
                            t.commit()

                        } else {
                            Toast.makeText(requireContext(), "Il pagamento non è andato a buon fine!", Toast.LENGTH_LONG).show()
                            Toast.makeText(requireContext(), "Carta generata", Toast.LENGTH_SHORT).show()
                            Utente.getInstance().generateCarta()
                        }

                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                        Utente.getInstance().clearCarta().generateCarta()
                    }
                }
                3 -> {

                }
                else -> {
                    Toast.makeText(
                        requireContext(),
                        "Seleziona un metodo di pagamento",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }


    }

    override fun onResume() {
        super.onResume()

        val checkBox = binding.fragResocontoModPagamento.findViewById<CheckBox>(R.id.checkBoxPagaInAnticipo)
        checkBox.isChecked = true
        checkBox.isClickable = false
    }


}