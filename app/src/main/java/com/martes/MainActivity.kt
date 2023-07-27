package com.martes

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.martes.presentation.authentication.LoginActivity

class MainActivity : AppCompatActivity() {
    private val retardo: Long = 500
    private lateinit var nombre: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        verificarSesion()
        FirebaseMessaging.getInstance().subscribeToTopic("Pink")
    }



    private fun verificarSesion() {
        object : CountDownTimer(retardo, retardo) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                if (FirebaseAuth.getInstance().currentUser != null) {
                    nombreUsuario()
                    startActivity(Intent(this@MainActivity,
                        InicioActivity::class.java))
                } else {
                    startActivity(Intent(this@MainActivity,
                        LoginActivity::class.java))
                }
                finish()
            }
        }.start()
    }

    private fun nombreUsuario(){
        FirebaseFirestore.getInstance()
            .collection("usuario")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .get()
            .addOnSuccessListener {
                nombre = it.getString("nombres").toString()
                toast("Hola $nombre")
            }
            .addOnFailureListener{
                toast("${it.message}")
            }
    }

    private fun toast(mensaje: String) {
        Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT).show()
    }
}
