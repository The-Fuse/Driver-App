package com.example.driver_app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registeration.*
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startmyday.setOnClickListener {
            checkbusno()
        }
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ffffff")))
        val user = Firebase.auth.currentUser
        if (user != null){
            Snackbar.make(startmyday, "Logged in Successfully !!", Snackbar.LENGTH_SHORT).show()
        }else {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
        val db = Firebase.firestore
        val uid = user?.uid.toString()
        val ref = db.collection("Drivers").document(uid)
        ref.get()
            .addOnSuccessListener { document ->
                if (document !=null){
                    val username = document.get("name")
                    supportActionBar?.setTitle("Hello $username")
                }
            }
    }
    private fun checkbusno(){
        val busno = bus_number.text.toString()
        if (busno.isNotEmpty()) {
            Firebase.firestore.collection("Buses").get().addOnSuccessListener { documents ->
                for (document in documents) {
                    if (busno == document.id) {
                        val driverassigned = document.get("DriverAssigned")
                        if (driverassigned == false) {
                            val uid  = Firebase.auth.currentUser?.uid
                            val ref = Firebase.firestore.collection("Buses").document(document.id)
                            ref.update("DriverAssigned",true,"driver_id",uid)
                            val intent = Intent(this,home_page::class.java)
                            startActivity(intent)
                        } else {
                            Snackbar.make(
                                startmyday,
                                "Driver already Assigned !!",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }


            }
        }else{
            Snackbar.make(startmyday, "Please enter the bus no.", Snackbar.LENGTH_SHORT).show()
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
class Locationdata(val longitude: String, val latitude: String)