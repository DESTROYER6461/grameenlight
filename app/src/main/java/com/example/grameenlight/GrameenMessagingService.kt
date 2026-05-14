package com.example.grameenlight

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class GrameenMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // Handle incoming FCM messages here
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Save new token to Firestore if needed
    }
}