package com.martes.presentation.authentication.google

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.martes.InicioActivity
import com.martes.R

class RegistoGoogleActivity : AppCompatActivity() {
    private lateinit var nombres: TextInputEditText
    private lateinit var paterno: TextInputEditText
    private lateinit var materno: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registo_google)

        nombres = findViewById(R.id.inputNombresG)
        paterno = findViewById(R.id.inputPaternoG)
        materno = findViewById(R.id.inputMaternoG)

        findViewById<Button>(R.id.btnRegistroG).setOnClickListener {
            validarCampos()
        }
    }

    private fun toast(mensaje: String) {
        Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun validarCampos() {
        val camposNoVacios = listOf(nombres, materno, paterno)
            .map { it.text.toString().trim().isNotEmpty() }
            .all { it }

        camposNoVacios.takeIf { it }?.let {
            guardarInformacionUsuarioG()
        } ?: toast("Campos vacíos")
    }

    private fun guardarInformacionUsuarioG() {
        FirebaseFirestore.getInstance()
            .collection("usuario")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .set(
                hashMapOf(
                    "nombres" to nombres.text.toString(),
                    "aPaterno" to paterno.text.toString(),
                    "aMaterno" to materno.text.toString()
                )
            ).addOnSuccessListener {
                startActivity(
                    Intent(
                        this@RegistoGoogleActivity,
                        InicioActivity::class.java
                    )
                )
                toast("Guardado")
            }
            .addOnFailureListener {
                toast("${it.message}")
            }
    }
}