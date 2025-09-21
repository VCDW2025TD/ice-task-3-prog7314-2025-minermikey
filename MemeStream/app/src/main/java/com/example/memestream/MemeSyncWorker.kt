package com.example.memestream

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.ListenableWorker.Result
import androidx.room.Room

class MemeSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Get the Room database
            val database = MemeDatabase.getDatabase(applicationContext)
            val memeDao = database.memeDao()

            // Create repository (replace with your Retrofit API instance)
            val repository = MemeRepository(
                memeDao = memeDao,
                memeApi = RetrofitInstance.api // <-- make sure you have a singleton Retrofit instance
            )

            // Sync unsynced memes
            repository.syncMemes()

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
