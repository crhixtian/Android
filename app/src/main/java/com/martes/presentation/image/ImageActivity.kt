package com.martes.presentation.image

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.martes.R

class ImageActivity : AppCompatActivity() {

    private val imagePicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            result.data?.data?.let {
                uploadImage(it)
            }
        }
    }
    private lateinit var btnUpload: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        btnUpload = findViewById(R.id.btnUpload)
        btnUpload.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        imagePicker.launch(intent)
    }


    private fun uploadImage(uri: Uri) {
        val image = FirebaseStorage.getInstance().reference.child("images/${System.currentTimeMillis()}_image.jpg")
        val user = FirebaseAuth.getInstance().currentUser?.email.toString()
        image.putFile(uri)
            .addOnSuccessListener {
                image.downloadUrl
                    .addOnSuccessListener {
                        if (user.isNotEmpty()) {
                            FirebaseFirestore.getInstance()
                                .collection("usuario")
                                .document(user)
                                .set(
                                    hashMapOf(
                                        "imageURL" to it.toString()
                                    ), SetOptions.merge()
                                )
                        }
                    }
            }
            .addOnFailureListener {
            }
    }
}