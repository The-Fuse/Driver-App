package com.example.driver_app

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home_page.*

class home_page : AppCompatActivity() {
    lateinit var locationmanager : LocationManager
    lateinit var location : Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ffffff")))
        supportActionBar?.setTitle("Your Bus Details")
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        }
            locationmanager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                location = locationmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
            }else{
                location = locationmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
            }
            val locationlistener = object: LocationListener {
                override fun onLocationChanged(p0: Location) {
                    reversegeoCode(p0)
                }

            }
        if (locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,100.2f,locationlistener)

        }else{
            locationmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100,100.2f,locationlistener)

        }

    }

    private fun reversegeoCode(location: Location?) {
        val uid  = Firebase.auth.currentUser?.uid.toString()
        val loca = Locationdata(location?.longitude.toString(), location?.latitude.toString())
        if (location != null) {
            Firebase.firestore.collection("Drivers").document(uid).update("Longitude",location.longitude,"Latitude",location.latitude)
                .addOnSuccessListener {
                    Snackbar.make(takeabreak, "Location Data feeds start", Snackbar.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Snackbar.make(takeabreak, "Failed location feed", Snackbar.LENGTH_SHORT).show()
                }
        }
    }
}