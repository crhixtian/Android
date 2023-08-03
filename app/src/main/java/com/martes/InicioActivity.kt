package com.martes

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.martes.presentation.assistance.AssistanceActivity
import com.martes.presentation.authentication.LoginActivity
import com.martes.presentation.image.ImageActivity
import com.martes.presentation.location.LocationActivity
import com.martes.presentation.maps.MapsActivity
import com.martes.presentation.profile.ProfileActivity

class InicioActivity : AppCompatActivity() {
    private lateinit var btnOut: Button
    private lateinit var btnPay: Button
    private lateinit var btnProfile: Button
    private lateinit var btnUp: Button
    private lateinit var btnMaps: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        btnOut = findViewById(R.id.btnOut)
        btnPay = findViewById(R.id.btnPay)
        btnProfile = findViewById(R.id.btnProfile)
        btnUp = findViewById(R.id.btnUp)
        btnMaps = findViewById(R.id.btnMaps)

        btnOut.setOnClickListener {
            cerrarSesion()
        }
        btnPay.setOnClickListener {
            startActivity(
                Intent(
                    this@InicioActivity,
                    AssistanceActivity::class.java
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
        btnUp.setOnClickListener {
            startActivity(
                Intent(
                    this@InicioActivity,
                    ImageActivity::class.java
                )
            )
        }
        btnMaps.setOnClickListener {
            startActivity(
                Intent(
                    this@InicioActivity,
                    MapsActivity::class.java
                )
            )
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener {
                if (!it.isSuccessful) {
                    Log.w(TAG, "FCM token failed", it.exception)
                    return@OnCompleteListener
                }
                Log.d(TAG, getString(R.string.msg_token_fmt, it.result))
            })

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