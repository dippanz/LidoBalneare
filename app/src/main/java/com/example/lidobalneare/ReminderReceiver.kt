package com.example.lidobalneare

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat


class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // Creare un ID per il canale di notifica
        val channelId = "canale_prenotazioni"
        val title = intent?.getStringExtra("title") ?: ""
        val desc = intent?.getStringExtra("desc") ?: ""

        // Creare un oggetto NotificationCompat.Builder
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notifica_foreground)
            .setContentTitle(title)
            .setContentText(desc)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Inviare la notifica al sistema
        val notificationManager: NotificationManager =
            context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(0, builder.build())


        //carico notifica sul db
        DBMSboundary().insertNotifica(title, desc, Utente.getInstance().getId())
    }
}