package com.martes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.martes.autenticacion.LoginActivity
import com.martes.presentacion.InicioActivity

class MainActivity : AppCompatActivity() {
    private val retardo: Long = 1000
    private lateinit var nombre: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        verificarSesion()
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
            .document(FirebaseAuth.getInstance().currentUser?.email!!)
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
