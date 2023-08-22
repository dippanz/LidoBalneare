package com.example.lidobalneare

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lidobalneare.databinding.FragProfiloBinding

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

        binding.textViewNome.text = Utente.getInstance().getNome()
        binding.textMail.text = resources.getString(R.string.email, Utente.getInstance().getEmail())
        binding.textNomeCognome.text = resources.getString(R.string.nome_e_cognome, "${Utente.getInstance().getNome()} ${Utente.getInstance().getCognome()}")
    }
}