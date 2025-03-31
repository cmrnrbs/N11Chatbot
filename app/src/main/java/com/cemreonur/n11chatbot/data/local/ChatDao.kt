package com.cemreonur.n11chatbot.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: ChatMessage)

    @Query("SELECT * FROM chat_messages ORDER BY id ASC")
    fun getAllMessages(): Flow<List<ChatMessage>>

    @Query("DELETE FROM chat_messages")
    suspend fun clearChatHistory()

    @Query("UPDATE chat_messages SET answeredStepName = :answeredStepName WHERE currentStep = :currentStep")
    suspend fun updateAnsweredStepName(currentStep: String, answeredStepName: String)
}