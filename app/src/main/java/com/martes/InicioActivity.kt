package com.martes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.martes.presentation.authentication.LoginActivity
import com.martes.presentation.stripe.CardActivity
import com.martes.profile.ProfileActivity

class InicioActivity : AppCompatActivity() {
    private lateinit var btnOut: Button
    private lateinit var btnPay: Button
    private lateinit var btnProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        btnOut = findViewById(R.id.btnOut)
        btnPay = findViewById(R.id.btnPay)
        btnProfile = findViewById(R.id.btnProfile)

        btnOut.setOnClickListener {
            cerrarSesion()
        }
        btnPay.setOnClickListener {
            startActivity(
                Intent(
                    this@InicioActivity,
                    CardActivity::class.java
                )
            )
        }
        btnProfile.setOnClickListener {
            startActivity(
                Intent(
                    this@InicioActivity,
                    ProfileActivity::class.java
                )
            )
        }
    }

    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)
            .signOut()
            .addOnCompleteListener {
                startActivity(
                    Intent(
                        this@InicioActivity,
                        LoginActivity::class.java
                    )
                )
                finish()
            }
    }

}