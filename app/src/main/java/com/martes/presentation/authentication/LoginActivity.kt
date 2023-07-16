package com.martes.presentation.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.martes.R
import com.martes.presentation.authentication.google.RegistoGoogleActivity
import com.martes.InicioActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var correo: TextInputEditText
    private lateinit var contrasena: TextInputEditText
    private lateinit var textRegistro: TextView
    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                GoogleSignIn.getSignedInAccountFromIntent(result.data).let {
                    try {
                        it.getResult(ApiException::class.java)?.let { account ->
                            firebaseAuthWithGoogle(account)
                        } ?: toast("${it.exception?.message}")
                    } catch (e: ApiException) {
                        toast("${e.message}")
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        correo = findViewById(R.id.inputCorreoL)
        contrasena = findViewById(R.id.inputContrasenaL)
        textRegistro = findViewById(R.id.textRegistro)

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            validarCampos()
        }

        findViewById<Button>(R.id.btnGoogle).setOnClickListener {
            iniciarSesionGoogle()
        }

        findViewById<Button>(R.id.btnFacebook).visibility = View.GONE

        textRegistro.setOnClickListener {
            startActivity(
                Intent(
                    this@LoginActivity,
                    RegistroActivity::class.java
                )
            )
        }
    }

    private fun toast(mensaje: String) {
        Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun validarCampos() {
        if (correo.text.toString().trim().isNotEmpty()
            && contrasena.text.toString().trim().isNotEmpty()
        ) {
            iniciarSesion()
        } else {
            toast("Campos vacios")
        }
    }

    private fun iniciarSesion() {
        lateinit var nombre: String
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            correo.text.toString().trim(),
            contrasena.text.toString().trim()
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    FirebaseFirestore.getInstance()
                        .collection("usuario")
                        .document(FirebaseAuth.getInstance().currentUser?.email!!)
                        .get()
                        .addOnSuccessListener { usuario ->
                            nombre = usuario.getString("nombres").toString()
                            toast("Hola ${nombre}!")
                            startActivity(
                                Intent(
                                    this@LoginActivity,
                                    InicioActivity::class.java
                                )
                            )
                        }
                        .addOnFailureListener { e ->
                            toast("${e.message}")
                        }
                } else {
                    toast("${it.exception?.message}")
                }
            }
    }

    private fun iniciarSesionGoogle() {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build().let { signInOptions ->
                GoogleSignIn.getClient(this, signInOptions).signInIntent.let { signInIntent ->
                    signInLauncher.launch(signInIntent)
                }
            }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        GoogleAuthProvider.getCredential(account.idToken, null).let { credential ->
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        FirebaseFirestore.getInstance()
                            .collection("usuario")
                            .document(FirebaseAuth.getInstance().currentUser?.email!!)
                            .get()
                            .addOnSuccessListener { usuario ->
                                if (usuario.getString("nombres") != null) {
                                    toast("Hola ${usuario.getString("nombres")}")
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            InicioActivity::class.java
                                        )
                                    )
                                } else {
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            RegistoGoogleActivity::class.java
                                        )
                                    )
                                }
                            }
                            .addOnFailureListener { e ->
                                toast("${e.message}")
                            }
                    } else {
                        toast("${it.exception?.message}")
                    }
                }
        }
    }

}