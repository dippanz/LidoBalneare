package com.example.lidobalneare

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lidobalneare.databinding.ActivityLogginBinding

class LogginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSubmit.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()

            val realeUsername = "admin"
            val realePassword = "admin"

            // Valida l'input dell'utente
            if (username == realeUsername && password == realePassword) {
                //val intent = Intent(this, ActivityAmministratore::class.java)
                //startActivity(intent)
            } else {
                Toast.makeText(this, "Username o Password errata", Toast.LENGTH_LONG).show()
            }
        }
    }
}
