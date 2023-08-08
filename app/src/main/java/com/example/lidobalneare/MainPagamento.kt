package com.example.lidobalneare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lidobalneare.databinding.ActivityMainPagamentoBinding

class MainPagamento : AppCompatActivity() {
    private lateinit var binding: ActivityMainPagamentoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPagamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()

        if(Utente.getInstance().isLoggedIn()){
            transaction.replace(R.id.pagamento_view_frag, FragModPagamento())
            val b = Bundle()
            b.putSerializable("viewModel", intent.getSerializableExtra("viewModel"))
            manager.findFragmentById(R.id.pagamento_view_frag)?.arguments = b
        }else{
            transaction.replace(R.id.pagamento_view_frag, FragInserisciDatiPagamento())
        }

        transaction.commit()
    }
}