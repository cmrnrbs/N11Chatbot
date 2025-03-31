package com.cemreonur.n11chatbot.di

import com.cemreonur.n11chatbot.data.ChatRepository
import com.cemreonur.n11chatbot.data.local.ChatDao
import com.cemreonur.n11chatbot.data.remote.WebSocketClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /*@Provides
    @Singleton
    fun provideChatRepository(
        chatDao: ChatDao,
        webSocketClient: WebSocketClient
    ): ChatRepository {
        return ChatRepository(chatDao, webSocketClient)
    }*/
}