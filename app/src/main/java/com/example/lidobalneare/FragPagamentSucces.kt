package com.example.lidobalneare

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class FragPagamentSucces: Fragment(R.layout.pagamento_succes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.textViewPagSucess).text =
            arguments?.getString("valueText") ?: resources.getString(R.string.pagamento_avvenuto_con_successo)
    }
}