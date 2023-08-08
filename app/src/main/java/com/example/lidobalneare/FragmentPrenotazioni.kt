package com.example.pmwhotel3
/*
package com.example.pmwhotel3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pmwhotel3.databinding.FragmentDatTestoBinding

class FragmentDatiTesto : Fragment() {
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

        DBMSboundary().getPrenotazioni(requireContext(), object : QueryReturnCallback<Any?> {
            override fun onReturnValue(response: Any, message: String) {
                // Aggiungi i dati alla lista cardDataList
                if (response is List<*>) {
                    for (item in response) {
                        if (item is Prenotazioni) {

                            val cardData = GestoreDatiTesto(
                                item.nome,
                                item.cognome,
                                item.dataInizio,
                                item.dataFine
                            )
                            data.add(cardData)
                        }
                    }
                    // Aggiorna l'adapter della RecyclerView
                    AdapterText.notifyDataSetChanged()
                }
            }

            override fun onQueryFailed(fail: String) {
                // Gestisci il caso in cui la query non abbia avuto successo
                Toast.makeText(requireContext(), fail, Toast.LENGTH_SHORT).show()
            }

            override fun onQueryError(error: String) {
                // Gestisci l'errore nella query
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        })

        // Assuming that your AdapterText takes an ArrayList of GestoreDatiTesto as a constructor parameter
        adapter = AdapterText(data)
        binding.recyclerview.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
*/
