package com.martes.repository.user

import com.martes.models.User

interface UserRepository {
    fun recoverProfileData(
        onSuccess: (User) -> Unit
    )
}