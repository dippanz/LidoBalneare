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

            Log.i("msg", "mail: $email")
            Log.i("msg", "loggato dopo mail: ${Utente.getInstance().isLoggedIn()}")

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
                                TODO("Not yet implemented")
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
                        Toast.makeText(requireContext(), "Credenziali errate, riprovare!", Toast.LENGTH_LONG).show()
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




        }

        binding.includedRegister.registerButton.setOnClickListener {
            val list = ArrayList<String>()
            val viewRegister = binding.includedRegister
            if(DBMSboundary().isPasswordStrong(viewRegister.editTextPass.text.toString())
                && viewRegister.editTextNome.text.isNotEmpty()
                && viewRegister.editTextCognome.text.isNotEmpty()
                && DBMSboundary().isPhoneNumberValid(viewRegister.editTextTel.text.toString())
                && DBMSboundary().isEmailValid(viewRegister.editTextMail.text.toString())){

                list.add(viewRegister.editTextNome.text.toString())
                list.add(viewRegister.editTextCognome.text.toString())
                list.add(viewRegister.editTextTel.text.toString())
                list.add(viewRegister.editTextMail.text.toString())
                //todo usare crittografia per la password
                list.add(viewRegister.editTextPass.text.toString())

                DBMSboundary().insertUtente(requireContext(), object : QueryReturnCallback<Int>{
                    override fun onReturnValue(response: Int, message: String) {
                        if(response == 200){
                            Toast.makeText(requireContext(), "inserito correttamente", Toast.LENGTH_SHORT).show()

                            //ritorno sempre alla home dopo l'accesso
                            if(Utente.getInstance().isLoggedIn()){
                                val intent = Intent(requireContext(), MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                requireActivity().finish()
                                startActivity(intent)
                            }
                        }else{
                            Toast.makeText(requireContext(), "errore inserimento", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onQueryFailed(fail: String) {
                        Toast.makeText(requireContext(), fail, Toast.LENGTH_SHORT).show()
                    }

                    override fun onQueryError(error: String) {
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()

                    }
                }, list)

            }else{
                Toast.makeText(context, "inserire tutti i dati correttamente", Toast.LENGTH_SHORT).show()
            }
        }
    }










}