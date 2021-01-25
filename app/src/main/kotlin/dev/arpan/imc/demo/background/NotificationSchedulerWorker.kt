package dev.arpan.imc.demo.background

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import dev.arpan.imc.demo.utils.Notify
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NotificationSchedulerWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) :
    Worker(context, workerParameters) {

    companion object {
        private const val UNIQUE_WORK_NAME = "NotificationSchedulerWorker"

        fun start(context: Context, delay: Long) {
            val workManager = WorkManager.getInstance(context)
            OneTimeWorkRequest.Builder(NotificationSchedulerWorker::class.java)
            val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
                NotificationSchedulerWorker::class.java
            ).setInitialDelay(delay, TimeUnit.MILLISECONDS).build()
            workManager.enqueueUniqueWork(
                UNIQUE_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                oneTimeWorkRequest
            )
        }

        fun calculateDelay(triggerMillis: Long): Long {
            val triggerCalendar = Calendar.getInstance().apply {
                timeInMillis = triggerMillis
            }
            val nowMillis = Calendar.getInstance().apply {
                if (triggerCalendar[Calendar.MINUTE] == this[Calendar.MINUTE]) {
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
            }.timeInMillis

            return triggerCalendar.timeInMillis - nowMillis
        }
    }

    override fun doWork(): Result {
        Notify(context.applicationContext).pushNotification()
        return Result.success()
    }
}
