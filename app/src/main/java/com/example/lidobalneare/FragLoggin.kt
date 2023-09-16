package com.example.lidobalneare


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.lidobalneare.databinding.FragLogginBinding

class FragLoggin: Fragment(R.layout.frag_loggin) {

    private lateinit var binding: FragLogginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragLogginBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageLoginBack.setOnClickListener {
            requireActivity().finish()
        }

        binding.includedLogin.linkRegistrazione.setOnClickListener {
            binding.includedLogin.root.visibility = View.GONE
            binding.includedRegister.root.visibility = View.VISIBLE
        }

        binding.includedRegister.linkLogin.setOnClickListener {
            binding.includedLogin.root.visibility = View.VISIBLE
            binding.includedRegister.root.visibility = View.GONE
        }

        binding.includedLogin.loginButton.setOnClickListener {
            val email = binding.includedLogin.editTextEmail.text.toString()
            val pass = binding.includedLogin.editTextPassword.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty()){
                //recupero un account con questa mail se non è presente butto messaggio di errore
                DBMSboundary().getCredenziali(requireContext(), object : QueryReturnCallback<Utente>{
                    override fun onReturnValue(response: Utente, message: String) {
                        //ho trovato la corrispondenza
                        if(response.getId() != -1){
                            //setto il log
                            Utente.getInstance().setLoggedIn()

                            //faccio altra query per avere il resto delle informazioni per l'utente
                            DBMSboundary().getAltreInfoUtente(requireContext(), object : QueryReturnCallback<Int>{
                                override fun onReturnValue(response: Int, message: String) {
                                }

                                override fun onQueryFailed(fail: String) {
                                    Toast.makeText(requireContext(), fail, Toast.LENGTH_SHORT).show()
                                }

                                override fun onQueryError(error: String) {
                                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                                }
                            }, response.getId().toString())

                            Log.i("msg", "entro qua, log fatto: ${Utente.getInstance().isLoggedIn()}")

                            //ritorno sempre alla home dopo l'accesso
                            if(Utente.getInstance().isLoggedIn()){
                                val intent = Intent(requireContext(), MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                requireActivity().finish()
                                startActivity(intent)
                            }

                        }else{
                            //log non effettuato
                            Toast.makeText(requireContext(),
                                getString(R.string.credenziali_errate_riprovare), Toast.LENGTH_LONG).show()
                            //pulisco gli edittext
                            binding.includedLogin.editTextEmail.text.clear()
                            binding.includedLogin.editTextPassword.text.clear()
                        }
                    }

                    override fun onQueryFailed(fail: String) {
                        Toast.makeText(requireContext(), fail, Toast.LENGTH_SHORT).show()
                    }

                    override fun onQueryError(error: String) {
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }

                }, email, pass)
            }else{
                Toast.makeText(requireContext(),
                    getString(R.string.inserire_dati), Toast.LENGTH_LONG).show()
            }
        }

        binding.includedRegister.registerButton.setOnClickListener {
            val viewRegister = binding.includedRegister

            val password = viewRegister.editTextPass.text.toString()
            val nome = viewRegister.editTextNome.text.toString()
            val cognome = viewRegister.editTextCognome.text.toString()
            val telefono = viewRegister.editTextTel.text.toString()
            val email = viewRegister.editTextMail.text.toString()

            if (!DBMSboundary().isPasswordStrong(password)) {
                Toast.makeText(context, "La password non è abbastanza sicura", Toast.LENGTH_SHORT).show()
            } else if (nome.isEmpty()) {
                Toast.makeText(context, "Il campo 'Nome' è vuoto", Toast.LENGTH_SHORT).show()
            } else if (cognome.isEmpty()) {
                Toast.makeText(context, "Il campo 'Cognome' è vuoto", Toast.LENGTH_SHORT).show()
            } else if (!DBMSboundary().isPhoneNumberValid(telefono)) {
                Toast.makeText(context, "Il numero di telefono non è valido", Toast.LENGTH_SHORT).show()
            } else if (!DBMSboundary().isEmailValid(email)) {
                Toast.makeText(context, "L'indirizzo email non è valido", Toast.LENGTH_SHORT).show()
            } else {
                // Nessun errore, esegui la query
                val list = ArrayList<String>()
                list.add(nome)
                list.add(cognome)
                list.add(telefono)
                list.add(email)
                list.add(password)

                DBMSboundary().insertUtente(requireContext(), object : QueryReturnCallback<Int> {
                    override fun onReturnValue(response: Int, message: String) {
                        if (response == 200) {
                            Toast.makeText(requireContext(),
                                getString(R.string.inserito_correttamente), Toast.LENGTH_SHORT).show()

                            DBMSboundary().getCredenziali(requireContext(), object : QueryReturnCallback<Utente>{
                                override fun onReturnValue(response: Utente, message: String) {
                                    if(response.getId() != -1){
                                        Utente.getInstance().setLoggedIn()
                                        val intent = Intent(requireContext(), MainActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                        requireActivity().finish()
                                        startActivity(intent)
                                    }
                                }

                                override fun onQueryFailed(fail: String) {
                                    Toast.makeText(requireContext(), fail, Toast.LENGTH_SHORT).show()
                                }

                                override fun onQueryError(error: String) {
                                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                                }
                            }, email, password)

                        } else {
                            Toast.makeText(requireContext(),
                                getString(R.string.errore_inserimento), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onQueryFailed(fail: String) {
                        Toast.makeText(requireContext(), fail, Toast.LENGTH_SHORT).show()
                    }

                    override fun onQueryError(error: String) {
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }
                }, list)
            }
        }
    }













}