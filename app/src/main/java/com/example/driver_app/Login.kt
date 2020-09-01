package com.example.driver_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        registrationtransfer.setOnClickListener {
            val intent = Intent(this,Registeration::class.java)
            startActivity(intent)
        }
    }
}