package com.example.lidobalneare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.lidobalneare.databinding.ViewNumPersonNestedBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class NumPersonFragment: Fragment(R.layout.view_num_person_nested) {

    private lateinit var binding: ViewNumPersonNestedBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!requireActivity().findViewById<TextView>(R.id.imageButtonLettino).isVisible){
            binding.textView3.visibility = View.GONE
            binding.linearLayout3.visibility = View.GONE
        }

        binding.add1.setOnClickListener {
            val textView = binding.textNumVariabile1
            if(textView.text.toString().toInt() < 9){
                val num = textView.text.toString().toInt() + 1
                textView.text = num.toString()
            }else{
                Toast.makeText(requireContext(), "Numero massimo raggiunto", Toast.LENGTH_SHORT).show()
            }
        }

        binding.add2.setOnClickListener {
            val textView = binding.textNumVariabile2
            if(textView.text.toString().toInt() < 9){
                val num = textView.text.toString().toInt() + 1
                textView.text = num.toString()
            }else{
                Toast.makeText(requireContext(), "Numero massimo raggiunto", Toast.LENGTH_SHORT).show()
            }
        }

        binding.meno1.setOnClickListener {
            val textView = binding.textNumVariabile1
            if(textView.text.toString().toInt() > 1){
                val num = textView.text.toString().toInt() - 1
                textView.text = num.toString()
            }else{
                Toast.makeText(requireContext(), "Numero minimo raggiunto", Toast.LENGTH_SHORT).show()
            }
        }

        binding.meno2.setOnClickListener {
            val textView = binding.textNumVariabile2
            if(textView.text.toString().toInt() > 1){
                val num = textView.text.toString().toInt() - 1
                textView.text = num.toString()
            }else{
                Toast.makeText(requireContext(), "Numero minimo raggiunto", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonApplica.setOnClickListener {
            ( activity as? MainPrenotazione)?.getBottomSheet()?.state = BottomSheetBehavior.STATE_COLLAPSED
            requireActivity().findViewById<TextView>(R.id.textNumPersone)?.text = resources.getString(R.string.persone_1s, binding.textNumVariabile1.text)
            requireActivity().findViewById<TextView>(R.id.textNumLettini)?.text = resources.getString(R.string.lettini_1_s, binding.textNumVariabile2.text)
        }





    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ViewNumPersonNestedBinding.inflate(inflater, container, false)
        return binding.root
    }


}