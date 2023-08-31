package com.example.lidobalneare

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.lidobalneare.databinding.FragProfiloBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

class FragProfilo: Fragment(R.layout.frag_profilo) {

    private lateinit var binding: FragProfiloBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragProfiloBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textImpostazioni.setOnClickListener {
            val i = Intent(requireContext(), MainAreaPersonale::class.java)
            i.putExtra("frag", "impostazioni")
            startActivity(i)
        }

        binding.textLemierecensioni.setOnClickListener {
            val i = Intent(requireContext(), MainAreaPersonale::class.java)
            i.putExtra("frag", "lemierecensioni")
            startActivity(i)
        }

        DBMSboundary().getPrenotazioni(requireContext(), object : QueryReturnCallback<List<ModelPrenotazione>>{
            override fun onReturnValue(response: List<ModelPrenotazione>, message: String) {
                binding.textViewNumEffettuate.text = response.size.toString()
                var i = 0
                val dateFormat = SimpleDateFormat("EEE dd MMM", Locale.ITALIAN)
                for (e in response){
                    val date = dateFormat.parse(e.data)
                    val localDate =
                        date?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()?.withYear( LocalDate.now().year)
                    Log.i("msg", localDate.toString())
                    if(localDate != null && localDate.isAfter(LocalDate.now())){
                        i++
                    }

                }
                binding.textViewNumAttive.text = i.toString()
            }

            override fun onQueryFailed(fail: String) {
                Toast.makeText(context, fail, Toast.LENGTH_SHORT).show()
            }

            override fun onQueryError(error: String) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        }, Utente.getInstance().getId())
    }

    override fun onResume() {
        super.onResume()
        binding.textViewNome.text = Utente.getInstance().getNome()
        binding.textMail.text = resources.getString(R.string.email, Utente.getInstance().getEmail())
        binding.textNomeCognome.text = resources.getString(R.string.nome_e_cognome, "${Utente.getInstance().getNome()} ${Utente.getInstance().getCognome()}")
    }
}