package com.cemreonur.n11chatbot.data.domain

import android.content.Context
import com.cemreonur.n11chatbot.data.ChatRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
    private val gson: Gson
) {

    private val _chatSteps = MutableStateFlow<List<ChatMessageModel>>(emptyList())
    val chatSteps: StateFlow<List<ChatMessageModel>> = _chatSteps.asStateFlow()


    fun loadChatFlow(context: Context): List<ChatMessageModel> {
        val json = chatRepository.getChatFlowJson(context)
        val parsedSteps = parseChatFlowJson(json)

        _chatSteps.value = parsedSteps

        return parsedSteps
    }

    private fun parseChatFlowJson(json: String): List<ChatMessageModel> {
        return gson.fromJson(json, Array<ChatMessageModel>::class.java).toList()
    }

    private fun sendMessageToWebSocket(message: String) {
        chatRepository.sendMessageToWebSocket(message)
    }

    suspend fun processUserChoice(currentStep: String, action: String) {
        val nextStep = _chatSteps.value.find { it.step == action }
        /*if (nextStep?.action == "end_conversation") {
            _chatSteps.value = emptyList()
            return
        }*/

        //sendMessageToWebSocket(action)

        //TODO: mevcut step'e verilen cevabı update ediyorum ki daha sonra açıldığında buttonlar disable olsun ve ui güncellensin
        chatRepository.updateCurrentStep(currentStep, action)


        if (nextStep != null) {
            val jsonStep = gson.toJson(nextStep)
            sendMessageToWebSocket(jsonStep)
            _chatSteps.value = _chatSteps.value + nextStep
        }
    }


}
