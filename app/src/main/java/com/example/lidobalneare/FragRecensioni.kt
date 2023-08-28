package com.example.lidobalneare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lidobalneare.databinding.FragRecensioniBinding

class FragRecensioni: Fragment(R.layout.frag_recensioni) {

    private lateinit var binding: FragRecensioniBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragRecensioniBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val serializableList = arguments?.getSerializable("response") as? List<*>
        val list = serializableList?.filterIsInstance<ModelRecensioni>()

        if(list != null){
            binding.addRecensione.visibility = View.VISIBLE
            binding.titleRecensioni.text = resources.getString(R.string.title_recensioni)
            binding.recyclerviewRecensioni.visibility = View.VISIBLE
            binding.nessunaRecensioneAvviso.visibility = View.GONE
            binding.recyclerviewRecensioni.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerviewRecensioni.adapter =  AdapterRecensioni(requireContext(), list)

            binding.buttonRecensione.setOnClickListener {

                //carico recensione
                arguments?.let { it1 ->
                    DBMSboundary().insertRecensione(requireContext(),
                        binding.editRecensioneTitolo.text.toString(),
                        binding.editRecensioneDesc.text.toString(),
                        Utente.getInstance().getNome(),
                        it1.getString("nomeServizio", ""),
                        binding.ratingBarRec.rating,
                        Utente.getInstance().getId()
                    )
                }

                binding.editRecensioneTitolo.setText("")
                binding.editRecensioneDesc.setText("")
                binding.ratingBarRec.rating = 0F
            }

        }else{
            //nascondo parti che non servono per vedere le proprie recensioni
            binding.addRecensione.visibility = View.GONE


            DBMSboundary().getRecensioniUtente(requireContext(), object : QueryReturnCallback<List<ModelRecensioni>>{
                override fun onReturnValue(response: List<ModelRecensioni>, message: String) {
                    if(response.isEmpty()){
                        binding.recyclerviewRecensioni.visibility = View.GONE
                        binding.nessunaRecensioneAvviso.visibility = View.VISIBLE

                    }else{
                        binding.recyclerviewRecensioni.visibility = View.VISIBLE
                        binding.nessunaRecensioneAvviso.visibility = View.GONE
                        binding.recyclerviewRecensioni.layoutManager = LinearLayoutManager(requireContext())
                        binding.recyclerviewRecensioni.adapter =  AdapterRecensioni(requireContext(), response)
                    }
                }

                override fun onQueryFailed(fail: String) {
                    Toast.makeText(requireContext(), fail, Toast.LENGTH_SHORT).show()
                }

                override fun onQueryError(error: String) {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            }, Utente.getInstance().getId())
        }


    }










}