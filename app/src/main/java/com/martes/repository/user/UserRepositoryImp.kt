package com.martes.repository.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.martes.models.User

class UserRepositoryImp : UserRepository {
    override fun recoverProfileData(
        onSuccess: (User) -> Unit
    ) {
        FirebaseFirestore.getInstance()
            .collection("usuario")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val nombres = it.getString("nombres")
                    val aPaterno = it.getString("aPaterno")
                    val aMaterno = it.getString("aMaterno")
                    val dni = it.getString("dni")
                    val tiempo = it.getTimestamp("time")
                    val distrito = it.getString("distrito")
                    onSuccess(
                        User(
                            nombres.toString(),
                            aPaterno.toString(),
                            aMaterno.toString(),
                            dni.toString(),
                            tiempo?.seconds.toString(),
                            distrito.toString()
                        )
                    )
                }
            }.addOnFailureListener {
                it.message.toString()
            }
    }
}