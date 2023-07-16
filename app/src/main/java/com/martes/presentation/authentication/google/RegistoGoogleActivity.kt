package com.martes.presentation.authentication.google

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.martes.R
import com.martes.InicioActivity

class RegistoGoogleActivity : AppCompatActivity() {
    private lateinit var nombres: TextInputEditText
    private lateinit var paterno: TextInputEditText
    private lateinit var materno: TextInputEditText
    private lateinit var dni: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registo_google)

        nombres = findViewById(R.id.inputNombresG)
        paterno = findViewById(R.id.inputPaternoG)
        materno = findViewById(R.id.inputMaternoG)
        dni = findViewById(R.id.inputDniG)

        findViewById<Button>(R.id.btnRegistroG).setOnClickListener {
            validarCampos()
        }
    }

    private fun toast(mensaje: String) {
        Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun validarCampos() {
        val camposNoVacios = listOf(nombres, materno, paterno, dni)
            .map { it.text.toString().trim().isNotEmpty() }
            .all { it }

        camposNoVacios.takeIf { it }?.let {
            guardarInformacionUsuarioG()
        } ?: toast("Campos vacíos")
    }

    private fun guardarInformacionUsuarioG() {
        FirebaseFirestore.getInstance()
            .collection("usuario")
            .document(FirebaseAuth.getInstance().currentUser?.email.toString())
            .set(
                hashMapOf(
                    "nombres" to nombres.text.toString(),
                    "aPaterno" to paterno.text.toString(),
                    "aMaterno" to materno.text.toString(),
                    "dni" to dni.text.toString()
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