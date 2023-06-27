package com.martes.autenticacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.martes.R
import com.martes.presentacion.InicioActivity

class RegistroActivity : AppCompatActivity() {
    private lateinit var nombres: TextInputEditText
    private lateinit var paterno: TextInputEditText
    private lateinit var materno: TextInputEditText
    private lateinit var dni: TextInputEditText
    private lateinit var correo: TextInputEditText
    private lateinit var contrasena: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        nombres = findViewById(R.id.inputNombresR)
        paterno = findViewById(R.id.inputPaternoR)
        materno = findViewById(R.id.inputMaternoR)
        dni = findViewById(R.id.inputDniR)
        correo = findViewById(R.id.inputCorreoR)
        contrasena = findViewById(R.id.inputContrasenaR)

        findViewById<TextView>(R.id.textLogin).setOnClickListener {
            startActivity(
                Intent(this@RegistroActivity,
                    LoginActivity::class.java
                )
            )
        }

        findViewById<Button>(R.id.btnRegistro).setOnClickListener {
            validarCampos()
        }
    }

    private fun toast(mensaje: String) {
        Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun validarCampos() {
        val camposNoVacios = listOf(correo, contrasena, nombres, materno, paterno, dni)
            .map { it.text.toString().trim().isNotEmpty() }
            .all { it }
        val correoValidado = correo.text.toString().trim()
        val contrasenaValidado = contrasena.text.toString().trim()

        camposNoVacios.takeIf { it }?.let {
            registrarUsuario(correoValidado, contrasenaValidado)
        } ?: toast("Campos vacíos")
    }


    private fun registrarUsuario(correo: String, contrasena: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    guardarInformacionUsuario()
                } else {
                    toast("${it.exception?.message}")
                }
            }
    }

    private fun guardarInformacionUsuario() {
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
                    Intent(this@RegistroActivity,
                        InicioActivity::class.java
                    )
                )
                enviarCorreoVerificacion()
                toast("Perfil creado")
            }
            .addOnFailureListener {
                toast("${it.message}")
            }
    }

    private fun enviarCorreoVerificacion() {
        FirebaseAuth.getInstance().currentUser!!
            .sendEmailVerification()
    }
}