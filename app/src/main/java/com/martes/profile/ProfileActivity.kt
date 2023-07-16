package com.martes.profile

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.martes.R
import com.martes.repository.user.UserRepositoryImp

class ProfileActivity : AppCompatActivity() {
    private lateinit var user: UserRepositoryImp
    private lateinit var nombres: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initialize()
        recoverData()

    }

    private fun initialize(){
        nombres = findViewById(R.id.tvNombres)
        user = UserRepositoryImp()
    }

    private fun recoverData() {
        user.recoverProfileData(
            onSuccess = {
                nombres.text = it.nombres
            }
        )
    }
}