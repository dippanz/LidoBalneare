package com.example.lidobalneare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lidobalneare.databinding.ActivityMainSchermateVuoteBinding

class MainActivitySchermateVuote : AppCompatActivity() {

    private lateinit var binding: ActivityMainSchermateVuoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainSchermateVuoteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val manager = supportFragmentManager.beginTransaction()
        when(intent.getIntExtra("layout", 0)){
            R.layout.pagamento_succes -> {
                manager.replace(R.id.fragment_container_schermate_vuote, FragPagamentSucces())
            }

            R.layout.frag_loggin -> {
                manager.replace(R.id.fragment_container_schermate_vuote, FragLoggin())
            }

        }
        manager.commit()


    }
}