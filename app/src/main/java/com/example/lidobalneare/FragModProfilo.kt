package com.example.lidobalneare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.lidobalneare.databinding.FragModificaProfiloBinding

class FragModProfilo: Fragment(R.layout.frag_modifica_profilo) {

    private lateinit var binding: FragModificaProfiloBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragModificaProfiloBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textCambiaPass.setOnClickListener {
            if(binding.containerNuovapass.isVisible){
                binding.containerNuovapass.visibility = View.GONE
                binding.containerVecchiapass.visibility = View.GONE
            }else{
                binding.containerNuovapass.visibility = View.VISIBLE
                binding.containerVecchiapass.visibility = View.VISIBLE
            }
        }

        binding.buttonApplicaModProfilo.setOnClickListener {
            val list = ArrayList<String>()
            //nella lista vado inserendo i valori delle textView, se la editText è vuota la stringa ha lunghezza 0
            list.add(binding.editTextNomeModProfilo.text.toString())
            list.add(binding.editTextCognomeModProfilo.text.toString())
            list.add(binding.editTextTelModProfilo.text.toString())
            list.add(binding.editTextMailModProfilo.text.toString())
            list.add(binding.editTextVecchiaPass.text.toString())
            list.add(binding.editTextNewPass.text.toString())


            DBMSboundary().changeDatiUtente(requireContext(), object : QueryReturnCallback<Int>{
                override fun onReturnValue(response: Int, message: String) {
                    TODO("Not yet implemented")
                }

                override fun onQueryFailed(fail: String) {
                    TODO("Not yet implemented")
                }

                override fun onQueryError(error: String) {
                    TODO("Not yet implemented")
                }
            }, list, Utente.getInstance().getId())





        }
    }
}