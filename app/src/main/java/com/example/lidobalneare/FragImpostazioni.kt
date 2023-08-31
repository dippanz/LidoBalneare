package com.example.lidobalneare

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lidobalneare.databinding.FragImpostazioniBinding

class FragImpostazioni: Fragment(R.layout.frag_impostazioni) {

    private lateinit var binding: FragImpostazioniBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragImpostazioniBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textEsci.setOnClickListener {
            Utente.getInstance().clearUtente()
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            requireActivity().finish()
            startActivity(intent)
        }

        binding.textModificaProfilo.setOnClickListener {
            val t = parentFragmentManager.beginTransaction()
            t.addToBackStack("modProfilo")
            t.replace(R.id.containerAreaPersonale, FragModProfilo())
            t.commit()
        }

        binding.textMetodoPagamento.setOnClickListener {
            val t = parentFragmentManager.beginTransaction()
            t.addToBackStack("metodiPagamento")
            t.replace(R.id.containerAreaPersonale, FragInserisciDatiPagamento())
            t.commit()
        }
    }
}