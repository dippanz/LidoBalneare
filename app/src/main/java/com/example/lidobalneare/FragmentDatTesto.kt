package com.example.lidobalneare


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lidobalneare.databinding.FragmentDatTestoBinding

class FragmentDatTesto : Fragment() {
    private var _binding: FragmentDatTestoBinding? = null
    private val binding get() = _binding!!

    private val data = ArrayList<GestoreDatiTesto>()
    private lateinit var adapter: AdapterText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDatTestoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        /*
        DBMSboundary().getPrenotazioni(requireContext(), object : QueryReturnCallback<List<Prenotazioni>> {
            override fun onReturnValue(response: List<Prenotazioni>, message: String) {
                // Aggiungi i dati alla lista cardDataList
                for (item in response) {
                    val cardData = GestoreDatiTesto(item.nome,item.cognome,item.dataInizio,item.dataFine,item.camera )
                    data.add(cardData)
                }
                // Aggiorna l'adapter della RecyclerView
                adapter.notifyDataSetChanged()
            }

            override fun onQueryFailed(fail: String) {
                // Gestisci il caso in cui la query non abbia avuto successo
                Toast.makeText(requireContext(), fail, Toast.LENGTH_SHORT).show()
            }

            override fun onQueryError(error: String) {
                // Gestisci l'errore nella query
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        })*/

        // Assuming that your AdapterText takes an ArrayList of GestoreDatiTesto as a constructor parameter
        adapter = AdapterText(data)
        binding.recyclerview.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
