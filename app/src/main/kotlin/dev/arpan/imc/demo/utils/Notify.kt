package dev.arpan.imc.demo.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import dev.arpan.imc.demo.R
import dev.arpan.imc.demo.ui.main.MainActivity

private const val NOTIFICATION_ID = 101

class Notify(private val context: Context) {
    private val notificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun createNotificationChannel() {
        val channelId = context.getString(R.string.app_notification_channel_id)
        val channelDesc = context.getString(R.string.app_notification_channel_desc)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            notificationManager.getNotificationChannel(context.getString(R.string.app_notification_channel_id)) == null
        ) {
            val notificationChannel =
                NotificationChannel(channelId, channelDesc, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun pushNotification() {
        createNotificationChannel()

        val pendingIntent = NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.main_nav)
            .setDestination(R.id.nav_profile)
            .createPendingIntent()

        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.sample)
        val channelId = context.getString(R.string.app_notification_channel_id)
        val notification = NotificationCompat.Builder(context, channelId)
            .apply {
                setSmallIcon(R.drawable.ic_stat_notify)
                setContentTitle(context.getString(R.string.notification_title))
                setContentText(context.getString(R.string.notification_content))
                setAutoCancel(true)
                    .setStyle(
                        NotificationCompat.BigPictureStyle()
                            .setBigContentTitle(context.getString(R.string.notification_title))
                            .setSummaryText(context.getString(R.string.notification_content))
                            .bigPicture(bitmap)
                    )
                priority = NotificationCompat.PRIORITY_HIGH
                setContentIntent(pendingIntent)
            }
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
