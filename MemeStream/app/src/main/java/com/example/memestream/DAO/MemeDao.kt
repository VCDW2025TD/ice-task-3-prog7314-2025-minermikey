package com.example.memestream.DAO

import androidx.room.*
import com.example.memestream.DataaClasses.Meme

@Dao
interface MemeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeme(meme: Meme)

    @Update
    suspend fun updateMeme(meme: Meme)

    @Delete
    suspend fun deleteMeme(meme: Meme)

    @Query("SELECT * FROM memes ORDER BY timestamp DESC")
    suspend fun getAllMemes(): List<Meme>

    @Query("SELECT * FROM memes WHERE isSynced = 0")
    suspend fun getUnsyncedMemes(): List<Meme>
}
