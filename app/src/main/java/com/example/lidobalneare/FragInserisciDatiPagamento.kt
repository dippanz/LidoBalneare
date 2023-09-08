package com.example.lidobalneare

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.lidobalneare.databinding.FragInserisciDatiPagamentoBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class
FragInserisciDatiPagamento: Fragment(R.layout.frag_inserisci_dati_pagamento) {

    private lateinit var binding: FragInserisciDatiPagamentoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragInserisciDatiPagamentoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DBMSboundary().getDatiPagCarta(requireContext(), object : QueryReturnCallback<CartaPrepagata>{
            override fun onReturnValue(response: CartaPrepagata, message: String) {
                binding.textNumeroCarta.text = resources.getString(R.string.numero_carta_n_1_s, response.getNumeroCarta())
                binding.textCvv.text = resources.getString(R.string.cvv_n_1_s, response.getCvv())
                binding.textDataScadenza.text = resources.getString(R.string.data_di_scadenza_n_1_s, response.getDataScadeza().toString())
                binding.linearCarta.visibility = View.VISIBLE
                binding.cartaNonPresente.visibility = View.GONE
                binding.radioButtonCartaTendina.isClickable = false
            }

            override fun onQueryFailed(fail: String) {
                Toast.makeText(requireContext(), fail, Toast.LENGTH_SHORT).show()
            }

            override fun onQueryError(error: String) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                binding.linearCarta.visibility = View.GONE
                binding.cartaNonPresente.visibility = View.VISIBLE
                binding.cartaNonPresente.text = resources.getString(R.string._1_s_come_metodo_di_pagamento_non_presente, "Carta prepagata")
            }
        }, Utente.getInstance().getId())

        DBMSboundary().getDatiPagPaypal(requireContext(), object : QueryReturnCallback<PayPal>{
            override fun onReturnValue(response: PayPal, message: String) {
                binding.textMailPaypal.text = resources.getString(R.string.email_n_1_s, response.getEmail())
                binding.textNomeTitolarePaypal.text = resources.getString(R.string.nome_titolare_n_1_s, response.getNomeTitolare())
                binding.linearPaypal.visibility = View.VISIBLE
                binding.paypalNonPresente.visibility = View.GONE
                binding.radioButtonPayPalTendina.isClickable = false
            }

            override fun onQueryFailed(fail: String) {
                Toast.makeText(requireContext(), fail, Toast.LENGTH_SHORT).show()
            }

            override fun onQueryError(error: String) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                binding.linearPaypal.visibility = View.GONE
                binding.paypalNonPresente.visibility = View.VISIBLE
                binding.paypalNonPresente.text = resources.getString(R.string._1_s_come_metodo_di_pagamento_non_presente, "PayPal")
            }
        }, Utente.getInstance().getId())

        DBMSboundary().getDatiPagCC(requireContext(), object : QueryReturnCallback<ContoCorrente>{
            override fun onReturnValue(response: ContoCorrente, message: String) {
                binding.textNumConto.text = resources.getString(R.string.iban_n_1_s, response.getIban())
                binding.textNomeTitolareCC.text = resources.getString(R.string.nome_titolare_n_1_s, response.getNomeTitolare())
                binding.linearCC.visibility = View.VISIBLE
                binding.ccNonPresente.visibility = View.GONE
                binding.radioButtonBonificoTendina.isClickable = false
            }

            override fun onQueryFailed(fail: String) {
                Toast.makeText(requireContext(), fail, Toast.LENGTH_SHORT).show()
            }

            override fun onQueryError(error: String) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                binding.linearCC.visibility = View.GONE
                binding.ccNonPresente.visibility = View.VISIBLE
                binding.ccNonPresente.text = resources.getString(R.string._1_s_come_metodo_di_pagamento_non_presente, "Bonfico")
            }
        }, Utente.getInstance().getId())



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
                    val month: Int? = try {
                        input.substring(3, 5).toIntOrNull()
                    }catch (e: StringIndexOutOfBoundsException){
                        null
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
                    val trimmedInput = input.substring(0, 7)
                    editTextScadenza.setText(trimmedInput)
                    editTextScadenza.setSelection(trimmedInput.length) // Posiziona il cursore alla fine del testo
                }
            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()

                val textViewErroreData = view.findViewById<TextView>(R.id.textViewErroreData)
                val pattern = """\d{2}-\d{2}-\d{2}""".toRegex()
                if (input.length < 8 || !pattern.matches(input) ) {
                    textViewErroreData.setText(R.string.Inserisci_data_valida)
                }else{
                    textViewErroreData.text = ""
                }
            }
        })

        val editTextMailPayPal = view.findViewById<EditText>(R.id.editTextMailPayPal)
        editTextMailPayPal.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
                val textViewErroreEmail = view.findViewById<TextView>(R.id.textViewErroreMailPayPal)
                if(!emailPattern.matches(p0.toString())){
                    textViewErroreEmail.setText(R.string.formato_email_errato)
                }else{
                    textViewErroreEmail.text = ""
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        val editTextNumConto = view.findViewById<EditText>(R.id.editTextNumConto)
        editTextNumConto.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val ibanPattern = "^[A-Z]{2}\\d{2}[A-Za-z0-9]{1,30}\$".toRegex()
                val textViewErroreIban = view.findViewById<TextView>(R.id.textViewErroreNumConto)
                if(!ibanPattern.matches(p0.toString())){
                    textViewErroreIban.setText(R.string.formato_iabn_errato)
                }else{
                    textViewErroreIban.text = ""
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })



        //imposto visibilita punti inserimento dati
        val radioButtonCarta = view.findViewById<RadioButton>(R.id.radioButtonCartaTendina)
        var checkRadioButtonCarta = true
        //imposto i radioButton per caricare i dati
        radioButtonCarta.setOnClickListener { _ ->
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
        radioButtonPayPalTendina.setOnClickListener { _ ->
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
        radioButtonBonificoTendina.setOnClickListener { _ ->
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
            if(binding.radioButtonCartaTendina.isChecked &&
                binding.textViewErroreCvv.text == "" &&
                binding.textViewErroreCarta.text == "" &&
                binding.textViewErroreData.text == "") {

                val formatter = DateTimeFormatter.ofPattern("dd-MM-yy")
                DBMSboundary().insertDatiCarta(requireContext(),
                    binding.editTextNumero.text.toString(),
                    binding.editTextCvv.text.toString(),
                    LocalDate.parse(binding.editTextScadenza.text.toString(), formatter)
                )

                Utente.getInstance().setCarta(
                    binding.editTextNumero.text.toString(),
                    binding.editTextCvv.text.toString(),
                    LocalDate.parse(binding.editTextScadenza.text.toString(), formatter)
                    )
            }

            if(binding.radioButtonBonificoTendina.isChecked &&
                binding.textViewErroreNumConto.text == "") {
                DBMSboundary().insertCC(requireContext(), binding.editTextNumConto.text.toString())
                Utente.getInstance().setCc("0",
                    binding.editTextNumConto.text.toString(),
                    Utente.getInstance().getNome() + Utente.getInstance().getCognome()
                    )
            }

            if(binding.radioButtonPayPalTendina.isChecked &&
                binding.textViewErroreMailPayPal.text == "") {
                DBMSboundary().insertPaypal(requireContext(), binding.editTextMailPayPal.text.toString())
                Utente.getInstance().setPaypal("0",
                    binding.editTextMailPayPal.text.toString(),
                    Utente.getInstance().getNome() + Utente.getInstance().getCognome(),
                    Utente.getInstance().getTelefono()
                )
            }

            parentFragmentManager.popBackStack()
        }
    }

}