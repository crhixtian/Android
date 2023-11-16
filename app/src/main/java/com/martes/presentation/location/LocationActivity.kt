package com.martes.presentation.location

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

class LocationActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var btnLocation: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        btnLocation = findViewById(R.id.btnLocation)
        btnLocation.setOnClickListener {
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
        FirebaseAuth.getInstance().currentUser?.let {
            FirebaseFirestore.getInstance()
                .collection("usuario")
                .document(it.uid)
                .set(
                    hashMapOf(
                        "location" to GeoPoint(location.latitude, location.longitude),
                    ), SetOptions.merge()
                )
        }
    }
}