package com.cemreonur.n11chatbot.di

import com.cemreonur.n11chatbot.data.domain.ChatMessageModel
import com.cemreonur.n11chatbot.data.domain.ContentType
import com.cemreonur.n11chatbot.data.util.ContentDeserializer
import com.cemreonur.n11chatbot.data.util.ContentSerializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GsonModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(ChatMessageModel::class.java, ContentDeserializer())
            .registerTypeAdapter(ContentType::class.java, ContentSerializer())
            .create()
    }
}
