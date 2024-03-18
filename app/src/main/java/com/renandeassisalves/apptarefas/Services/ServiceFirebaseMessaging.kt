package com.renandeassisalves.apptarefas.Services

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.renandeassisalves.apptarefas.Activities.HomeActivity
import com.renandeassisalves.apptarefas.R

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
open class ServiceFirebaseMessaging : FirebaseMessagingService(){
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.data.isNotEmpty()) {
            Log.d("Notificação push de dados", "Dados recebidos pelo push: ${remoteMessage.data}")
        }

        val handler = Handler(Looper.getMainLooper())
        handler.post {
        remoteMessage.notification?.let {
            Log.d("Notificação push de mensagem", "Notificação push: ${it.body}")

            Toast.makeText(
                applicationContext, "${remoteMessage.notification?.title} ${remoteMessage.notification?.body}",
                Toast.LENGTH_SHORT
            ).show()
        }

            //Efetuar notificação em background
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

            val notificationBuilder = NotificationCompat.Builder(this, "MAIN")
                .setContentTitle(remoteMessage.notification?.title)
                .setContentText(remoteMessage.notification?.body)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(0, notificationBuilder.build())

        }
    }
}