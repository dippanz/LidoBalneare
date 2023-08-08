package com.example.lidobalneare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lidobalneare.databinding.FragmentServiziBinding

class FragmentServizi : Fragment() {
    private var _binding: FragmentServiziBinding? = null
    private val binding get() = _binding!!

    private val data = ArrayList<GestoreDati>()
    private lateinit var adapter: CustomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentServiziBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        for (i in 1..20) {
            data.add(GestoreDati("Testo della card $i", false))
        }

        adapter = CustomAdapter(data)
        binding.recyclerview.adapter = adapter

        adapter.setOnClickListener(object : CustomAdapter.OnClickListener {
            override fun onClick(position: Int, model: GestoreDati) {
                // Remove the item from the data list
                data.removeAt(position)

                // Notify the adapter that the item has been removed
                adapter.notifyItemRemoved(position)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}