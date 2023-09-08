package com.example.lidobalneare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            val nome = binding.editTextNomeModProfilo.text.toString()
            val cognome = binding.editTextCognomeModProfilo.text.toString()
            val telefono = binding.editTextTelModProfilo.text.toString()
            val email = binding.editTextMailModProfilo.text.toString()
            val oldPass = binding.editTextVecchiaPass.text.toString()
            val newPass = binding.editTextNewPass.text.toString()



            if(nome.isNotEmpty() ||
                cognome.isNotEmpty() ||
                telefono.isNotEmpty() ||
                email.isNotEmpty() ||
                (oldPass.isNotEmpty() && newPass.isNotEmpty())){

                //nella lista vado inserendo i valori delle textView, ordine: nome, cognome, tel, mail, oldP, newP
                list.add(nome)
                list.add(cognome)
                list.add(telefono)
                list.add(email)
                list.add(oldPass)
                list.add(newPass)

                DBMSboundary().changeDatiUtente(requireContext(), object : QueryReturnCallback<Int>{
                    override fun onReturnValue(response: Int, message: String) {
                        if(response == 200){
                            Toast.makeText(requireContext(),
                                getString(R.string.modifiche_applicate), Toast.LENGTH_SHORT).show()

                            val t = parentFragmentManager.beginTransaction()
                            t.replace(R.id.containerAreaPersonale, FragImpostazioni())
                            t.commit()
                        }else{
                            Toast.makeText(requireContext(),
                                getString(R.string.le_modifiche_non_sono_state_applicate), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onQueryFailed(fail: String) {
                        Toast.makeText(requireContext(), fail, Toast.LENGTH_SHORT).show()
                    }

                    override fun onQueryError(error: String) {
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }
                }, list, Utente.getInstance().getId())

            }else{
                Toast.makeText(context,
                    getString(R.string.inserisci_almeno_un_dato), Toast.LENGTH_SHORT).show()
            }







        }
    }
}