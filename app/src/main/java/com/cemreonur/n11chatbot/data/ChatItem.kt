package com.cemreonur.n11chatbot.data

import com.cemreonur.n11chatbot.data.domain.ChatMessageModel

sealed class ChatItem {
    //data class UserMessage(val message: ChatMessage) : ChatItem()
    data class StepMessage(val answeredStepName: String, val step: ChatMessageModel) : ChatItem()
}