package com.martes.presentacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.martes.R
import com.martes.autenticacion.LoginActivity

class InicioActivity : AppCompatActivity() {
    private lateinit var btnOut: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        btnOut = findViewById(R.id.btnOut)

        btnOut.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)
            .signOut()
            .addOnCompleteListener {
                startActivity(
                    Intent(this@InicioActivity,
                        LoginActivity::class.java))
                finish()
            }
    }

}