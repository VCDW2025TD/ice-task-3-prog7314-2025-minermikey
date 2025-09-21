package com.example.memestream

import com.example.memestream.DAO.MemeDao
import com.example.memestream.DataaClasses.Meme
import com.example.memestream.api.MemeApi

class MemeRepository(
    private val memeDao: MemeDao,
    private val memeApi: MemeApi // Retrofit API
) {

    suspend fun getMemes(online: Boolean): List<Meme> {
        return if (online) {
            val memes = memeApi.getMemes() // Fetch from API
            memes.forEach { memeDao.insertMeme(it) } // Cache locally
            memes
        } else {
            memeDao.getAllMemes()
        }
    }

    suspend fun addMeme(meme: Meme, online: Boolean) {
        if (online) {
            memeApi.postMeme(meme)
            memeDao.insertMeme(meme.copy(isSynced = true))
        } else {
            memeDao.insertMeme(meme) // Will sync later
        }
    }

    suspend fun syncMemes() {
        val unsynced = memeDao.getUnsyncedMemes()
        unsynced.forEach {
            memeApi.postMeme(it)
            memeDao.updateMeme(it.copy(isSynced = true))
        }
    }
}
