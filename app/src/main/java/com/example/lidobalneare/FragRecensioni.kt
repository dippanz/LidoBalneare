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

        DBMSboundary().getRecensioni(requireContext(), object : QueryReturnCallback<List<ModelRecensioni>>{
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