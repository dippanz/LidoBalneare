package com.example.lidobalneare

import android.os.Bundle
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
            0.1)
        transaction.replace(R.id.frag_resoconto_mod_pagamento, frag)
        transaction.commit()


        var radioButton = 0
        view.findViewById<RadioGroup>(R.id.radioGroup).setOnCheckedChangeListener { _, checkedId ->
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
                    try {
                        if (Utente.getInstance().getPaypal()?.effettuaPagamento(MyMoney(BigDecimal(importoTotale))) == true) {
                            //inserisco prenotazione nel db
                            val bundleQuery = requireArguments().getBundle("bundleQuery")
                            val startDate = bundleQuery?.getSerializable("startDate") as LocalDate
                            val endDate = bundleQuery.getSerializable("endDate") as LocalDate
                            val numPerson = bundleQuery.getInt("numPerson")
                            val nomeServizio = bundleQuery.getString("nomeServizio") as String

                            DBMSboundary().insertPrenotazione(requireContext(), true, startDate, endDate,
                                nomeServizio, Utente.getInstance().getId(), numPerson)

                            //se il pagamento è stato effettuato con successo
                            val t = parentFragmentManager.beginTransaction()
                            t.replace(R.id.fragment_container_schermate_vuote, FragPagamentSucces(), "FragPagamentSucces")
                            t.commit()

                        } else {
                            Toast.makeText(requireContext(),
                                getString(R.string.il_pagamento_non_andato_a_buon_fine_inserire_metodo_di_pagamento), Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(),
                            getString(R.string.soldi_non_sufficienti_caricare_altro_conto_paypal), Toast.LENGTH_SHORT).show()
                        Utente.getInstance().clearPaypal()

                    }

                }

                2 -> {
                    //carta
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

                            //se il pagamento è stato effettuato con successo
                            val t = parentFragmentManager.beginTransaction()
                            t.replace(R.id.fragment_container_schermate_vuote, FragPagamentSucces(), "FragPagamentSucces")
                            t.commit()

                        } else {
                            Toast.makeText(requireContext(), getString(R.string.il_pagamento_non_andato_a_buon_fine_inserire_metodo_di_pagamento), Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(),
                            getString(R.string.soldi_non_sufficienti_caricare_altra_carta), Toast.LENGTH_SHORT).show()
                        Utente.getInstance().clearCarta()

                    }
                }
                3 -> {
                    //conto corrente
                    try {
                        if (Utente.getInstance().getCc()?.effettuaPagamento(MyMoney(BigDecimal(importoTotale))) == true) {
                            //inserisco prenotazione nel db
                            val bundleQuery = requireArguments().getBundle("bundleQuery")
                            val startDate = bundleQuery?.getSerializable("startDate") as LocalDate
                            val endDate = bundleQuery.getSerializable("endDate") as LocalDate
                            val numPerson = bundleQuery.getInt("numPerson")
                            val nomeServizio = bundleQuery.getString("nomeServizio") as String

                            DBMSboundary().insertPrenotazione(requireContext(), true, startDate, endDate,
                                nomeServizio, Utente.getInstance().getId(), numPerson)

                            //se il pagamento è stato effettuato con successo
                            val t = parentFragmentManager.beginTransaction()
                            t.replace(R.id.fragment_container_schermate_vuote, FragPagamentSucces(), "FragPagamentSucces")
                            t.commit()

                        } else {
                            Toast.makeText(requireContext(), getString(R.string.il_pagamento_non_andato_a_buon_fine_inserire_metodo_di_pagamento), Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(),
                            getString(R.string.soldi_non_sufficienti_caricare_altro_conto_corrente), Toast.LENGTH_SHORT).show()
                        Utente.getInstance().clearCC()

                    }

                }
                else -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.seleziona_un_metodo_di_pagamento),
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