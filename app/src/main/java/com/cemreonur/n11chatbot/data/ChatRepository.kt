package com.cemreonur.n11chatbot.data

import android.content.Context
import com.cemreonur.n11chatbot.data.domain.ChatMessageModel
import com.cemreonur.n11chatbot.data.local.ChatDao
import com.cemreonur.n11chatbot.data.local.ChatMessage
import com.cemreonur.n11chatbot.data.remote.WebSocketClient
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val chatDao: ChatDao,
    private val webSocketClient: WebSocketClient,
    private val gson: Gson
) {

    private var isWebSocketConnected = false

    init {
        startListeningToWebSocket()
    }

    fun convertItem(string: String): ChatMessageModel {
        return gson.fromJson(string, ChatMessageModel::class.java)
    }

    suspend fun updateCurrentStep(currentStep: String, answeredStepName: String) {
        chatDao.updateAnsweredStepName(
            currentStep = currentStep,
            answeredStepName = answeredStepName
        )
    }

    fun closeWebSocket() {
        webSocketClient.closeConnection()
    }

    fun startListeningToWebSocket() {
        if (isWebSocketConnected) return

        webSocketClient.connect()
        isWebSocketConnected = true


        CoroutineScope(Dispatchers.IO).launch {
            webSocketClient.observeMessages().collect { message ->

                if (!message.contains("Request served")) {
                    val chatMessageModel = gson.fromJson(message, ChatMessageModel::class.java)
                    val botMessage =
                        ChatMessage(text = message, currentStep = chatMessageModel.step, answeredStepName = "")
                    chatDao.insert(botMessage)
                }
            }
        }
    }

    fun sendMessageToWebSocket(message: String) {
        webSocketClient.sendMessage(message)

        if (!isWebSocketConnected) {
            startListeningToWebSocket()
        }
    }

    fun getAllMessages(): Flow<List<ChatMessage>> {
        return chatDao.getAllMessages()
    }

    suspend fun clearChatHistory() {
        chatDao.clearChatHistory()
    }

    fun getChatFlowJson(context: Context): String {
        return context.assets.open("live_support_flow.json").bufferedReader().use { it.readText() }
    }

    suspend fun insertMessage(chatMessageModel: ChatMessageModel) {
        val jsonString = gson.toJson(chatMessageModel)
        chatDao.insert(
            message = ChatMessage(
                text = jsonString,
                currentStep = chatMessageModel.step,
                answeredStepName = ""
            )
        )
    }
}
