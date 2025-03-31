package com.cemreonur.n11chatbot.data.domain

data class ChatMessageModel(
    val step: String,
    val type: String,
    val content: ContentType,
    val action: String
)

sealed class ContentType {
    data class ButtonContent(
        val text: String,
        val buttons: List<Button>
    ) : ContentType()

    data class TextContent(
        val text: String
    ): ContentType()

    data class ImageContent(
        val imageUrl: String
    ) : ContentType()
}


data class Button(
    val label: String,
    val action: String
)
