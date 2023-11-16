package com.martes.presentation.maps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.martes.R

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        mapView = findViewById(R.id.mapVista)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        loadGeopointsFromFirestore()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun loadGeopointsFromFirestore(){
        FirebaseAuth.getInstance().currentUser?.let {
            FirebaseFirestore.getInstance()
                .collection("usuario")
                .document(it.uid)
                .collection("imagen")
                .get()
                .addOnSuccessListener { point ->
                    for (document in point){
                        val location = document.getGeoPoint("location")
                        if(location != null){
                            val latitude = location.latitude
                            val longitude = location.longitude
                            mMap.addMarker(MarkerOptions().position(LatLng(latitude, longitude)).title("Puntos"))
                        }
                    }
                }
        }
    }
}