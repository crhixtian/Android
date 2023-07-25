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
        val seconds = System.currentTimeMillis()/1000
        val user = FirebaseAuth.getInstance().currentUser?.email.toString()
        val image = FirebaseStorage.getInstance().reference.child("${user}/images/${seconds}.jpg")

        image.putFile(uri)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { url ->
                    if (user.isNotEmpty()) {
                        FirebaseFirestore.getInstance()
                            .collection("usuario")
                            .document(user)
                            .set(
                                hashMapOf(
                                    "imageURL" to url.toString()
                                ), SetOptions.merge()
                            )

                        FirebaseFirestore.getInstance()
                            .collection("usuario")
                            .document(user)
                            .collection("imagen")
                            .document(seconds.toString())
                            .set(
                            hashMapOf(
                                "imageURL" to url.toString()
                            )
                        )
                    }
                }
            }
            .addOnFailureListener {

            }
    }
}