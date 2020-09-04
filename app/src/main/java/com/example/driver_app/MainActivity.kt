package com.example.driver_app

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ffffff")))
        val user = Firebase.auth.currentUser
        if (user != null){
            Snackbar.make(startmyday, "Logged in Successfully !!", Snackbar.LENGTH_SHORT).show()
        }else {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menuaction, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.Logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
                true
            }else->super.onOptionsItemSelected(item)
        }
    }

}