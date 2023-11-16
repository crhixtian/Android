package com.martes.presentation.assistance

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.SetOptions
import com.martes.R

class AssistanceActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var btnAssistance: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assistance)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        btnAssistance = findViewById(R.id.btnAssistance)
        btnAssistance.setOnClickListener {
            if (checkPermissions()) {
                getLastLocation()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return if (permissions.all {
                ActivityCompat.checkSelfPermission(
                    this,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            true
        } else {
            ActivityCompat.requestPermissions(this, permissions, 1001)
            false
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    saveLocationToFirestore(location)
                }
            }
            .addOnFailureListener {
            }
    }

    private fun saveLocationToFirestore(location: Location) {
        val seconds = System.currentTimeMillis()/1000
        FirebaseAuth.getInstance().currentUser?.let {
            FirebaseFirestore.getInstance()
                .collection("usuario")
                .document(it.uid)
                .collection("imagen")
                .document(seconds.toString())
                .set(
                    hashMapOf(
                        "location" to GeoPoint(location.latitude, location.longitude),
                    ), SetOptions.merge()
                )
        }
    }
}