package com.example.lidobalneare

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.RadioGroup
import androidx.fragment.app.Fragment

class FragSpunte : Fragment(R.layout.frag_spunte) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<CheckBox>(R.id.checkBoxBarca).setOnCheckedChangeListener { compoundButton, b ->
            val group = view.findViewById<RadioGroup>(R.id.groupBarca)
            if(b){
                group.visibility = View.VISIBLE
            }else{
                group.visibility = View.GONE
            }
        }

        view.findViewById<CheckBox>(R.id.checkBoxGiroInBarca).setOnCheckedChangeListener { compoundButton, b ->
            val group = view.findViewById<RadioGroup>(R.id.groupGiroInBarca)
            if(b){
                group.visibility = View.VISIBLE
            }else{
                group.visibility = View.GONE
            }
        }


    }


}