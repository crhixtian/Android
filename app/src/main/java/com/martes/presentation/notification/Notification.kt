package com.martes.presentation.notification

import com.google.firebase.messaging.FirebaseMessagingService

class Notification: FirebaseMessagingService(){
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}