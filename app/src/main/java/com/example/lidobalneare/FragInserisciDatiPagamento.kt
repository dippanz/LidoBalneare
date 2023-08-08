package com.example.lidobalneare

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment

class
FragInserisciDatiPagamento: Fragment(R.layout.frag_inserisci_dati_pagamento) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //todo prendo dati e li inserisco nel db

        //setto gli editText specifici tipo di input solo numeri e controllo che vengano inseriti i dati corretti
        val editTextNumero = view.findViewById<EditText>(R.id.editTextNumero)
        editTextNumero.setRawInputType(InputType.TYPE_CLASS_NUMBER)
        editTextNumero.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Non utilizzato, lasciato vuoto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val maxLength = 16 // Limite di caratteri consentito
                val input = s.toString()

                if (input.length > maxLength) {
                    // Rimuovi i caratteri in eccesso
                    val trimmedInput = input.substring(0, maxLength)
                    editTextNumero.setText(trimmedInput)
                    editTextNumero.setSelection(trimmedInput.length) // Posiziona il cursore alla fine del testo

                }
            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()

                val textViewErroreCarta = view.findViewById<TextView>(R.id.textViewErroreCarta)
                if (input.length < 16) {
                    textViewErroreCarta.setText(R.string.Inserisci_numero_valido)
                }else{
                    textViewErroreCarta.text = ""
                }
            }
        })

        val editTextPagamentoTel = view.findViewById<EditText>(R.id.editTextPagamentoTel)
        editTextPagamentoTel.setRawInputType(InputType.TYPE_CLASS_NUMBER)
        editTextPagamentoTel.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Non utilizzato, lasciato vuoto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val maxLength = 10 // Limite di caratteri consentito
                val input = s.toString()

                if (input.length > maxLength) {
                    // Rimuovi i caratteri in eccesso
                    val trimmedInput = input.substring(0, maxLength)
                    editTextPagamentoTel.setText(trimmedInput)
                    editTextPagamentoTel.setSelection(trimmedInput.length) // Posiziona il cursore alla fine del testo

                }
            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()

                //controllo se i numeri inseriti sono 10
                if (input.length < 10) {
                    // Il testo inserito è composto da 10 cifre numeriche
                    // Puoi eseguire qui le azioni necessarie
                    // Esempio: mostra un messaggio di validazione
                    //todo fare qualcosa

                }else{

                }
            }
        })

        val editTextCvv = view.findViewById<EditText>(R.id.editTextCvv)
        editTextCvv.setRawInputType(InputType.TYPE_CLASS_NUMBER)
        editTextCvv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Non utilizzato, lasciato vuoto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val maxLength = 3 // Limite di caratteri consentito
                val input = s.toString()

                if (input.length > maxLength) {
                    // Rimuovi i caratteri in eccesso
                    val trimmedInput = input.substring(0, maxLength)
                    editTextCvv.setText(trimmedInput)
                    editTextCvv.setSelection(trimmedInput.length) // Posiziona il cursore alla fine del testo

                }
            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()

                val textViewErroreCvv = view.findViewById<TextView>(R.id.textViewErroreCvv)
                if (input.length < 3) {
                    textViewErroreCvv.setText(R.string.Inserisci_numero_valido)
                }else{
                    textViewErroreCvv.text = ""
                }
            }
        })

        val editTextScadenza = view.findViewById<EditText>(R.id.editTextScadenza)
        editTextScadenza.setRawInputType(InputType.TYPE_CLASS_NUMBER)
        editTextScadenza.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Non utilizzato, lasciato vuoto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString()

                if (input.length == 2 || input.length == 5) {
                    val day = input.substring(0, 2).toIntOrNull()
                    var month: Int?
                    try {
                        month = input.substring(3, 5).toIntOrNull()
                    }catch (e: StringIndexOutOfBoundsException){
                        month = null
                    }


                    // Controllo se il giorno è maggiore di 31
                    if ((day != null) && ((day > 31) || (day < 0))) {
                        // Blocco la scrittura reimpostando il testo precedente
                        editTextScadenza.setText(s?.substring(0, s.length - 1))
                        editTextScadenza.setSelection(editTextScadenza.length()) // Posiziona il cursore alla fine del testo
                    }// Controllo se il mese è maggiore di 12
                    else if ((month != null) && ((month > 12) || (month < 0))) {
                        // Blocco la scrittura reimpostando il testo precedente
                        editTextScadenza.setText(s?.substring(0, s.length - 1))
                        editTextScadenza.setSelection(editTextScadenza.length()) // Posiziona il cursore alla fine del testo
                    }else{
                        // Aggiungi il carattere "-" dopo i due caratteri o i cinque caratteri
                        val formattedInput = "$input-"
                        editTextScadenza.setText(formattedInput)
                        editTextScadenza.setSelection(formattedInput.length) // Posiziona il cursore alla fine del testo formattato
                    }

                }else if(input.length == 8){
                    val year = input.substring(6, 8).toIntOrNull()

                    //l'anno non deve essere superiore a 30 (perche oggi + 30 anni è abbastanza come scadenza)
                    if ((year != null) && ((year > 30) || (year < 0))) {
                        // Blocco la scrittura reimpostando il testo precedente
                        editTextScadenza.setText(s?.substring(0, s.length - 1))
                        editTextScadenza.setSelection(editTextScadenza.length()) // Posiziona il cursore alla fine del testo
                    }
                }
                else if (input.length > 8) {
                    // Rimuovi i caratteri in eccesso
                    val trimmedInput = input.substring(0, 8)
                    editTextScadenza.setText(trimmedInput)
                    editTextScadenza.setSelection(trimmedInput.length) // Posiziona il cursore alla fine del testo
                }


            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()

                val textViewErroreData = view.findViewById<TextView>(R.id.textViewErroreData)
                if (input.length < 8) {
                    textViewErroreData.setText(R.string.Inserisci_data_valida)
                }else{
                    textViewErroreData.text = ""
                }
            }
        })





        //imposto visibilita punti inserimento dati
        val radioButtonCarta = view.findViewById<RadioButton>(R.id.radioButtonCartaTendina)
        var checkRadioButtonCarta = true
        //imposto i radioButton per caricare i dati
        radioButtonCarta.setOnClickListener { v ->
            val linearLayoutCartaTendina = view.findViewById<LinearLayout>(R.id.linearLayoutCartaTendina)
            if(checkRadioButtonCarta){
                linearLayoutCartaTendina.visibility = View.VISIBLE
                checkRadioButtonCarta = false
            }else{
                linearLayoutCartaTendina.visibility = View.GONE
                checkRadioButtonCarta = true
                radioButtonCarta.isChecked = false
            }
        }

        val radioButtonPayPalTendina = view.findViewById<RadioButton>(R.id.radioButtonPayPalTendina)
        var checkRadioButtonPayPal = true
        //imposto i radioButton per caricare i dati
        radioButtonPayPalTendina.setOnClickListener { v ->
            val linearLayoutPayPalTendina = view.findViewById<LinearLayout>(R.id.linearLayoutPayPalTendina)
            if(checkRadioButtonPayPal){
                linearLayoutPayPalTendina.visibility = View.VISIBLE
                checkRadioButtonPayPal = false
            }else{
                linearLayoutPayPalTendina.visibility = View.GONE
                checkRadioButtonPayPal = true
                radioButtonPayPalTendina.isChecked = false
            }
        }

        val radioButtonBonificoTendina = view.findViewById<RadioButton>(R.id.radioButtonBonificoTendina)
        var checkRadioButtonBonifico = true
        //imposto i radioButton per caricare i dati
        radioButtonBonificoTendina.setOnClickListener { v ->
            val linearLayoutBonificoTendina = view.findViewById<LinearLayout>(R.id.linearLayoutBonificoTendina)
            if(checkRadioButtonBonifico){
                linearLayoutBonificoTendina.visibility = View.VISIBLE
                checkRadioButtonBonifico = false
            }else{
                linearLayoutBonificoTendina.visibility = View.GONE
                checkRadioButtonBonifico = true
                radioButtonBonificoTendina.isChecked = false
            }
        }

        view.findViewById<Button>(R.id.buttonInserisciDatiPagamenti).setOnClickListener {
            Utente.getInstance().setLoggedIn()
            requireActivity().onBackPressed()
        }
    }

}