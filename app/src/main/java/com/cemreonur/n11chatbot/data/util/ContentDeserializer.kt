package com.cemreonur.n11chatbot.data.util

import com.cemreonur.n11chatbot.data.domain.Button
import com.cemreonur.n11chatbot.data.domain.ChatMessageModel
import com.cemreonur.n11chatbot.data.domain.ContentType
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class ContentDeserializer : JsonDeserializer<ChatMessageModel> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ChatMessageModel {
        val jsonObject = json.asJsonObject
        val step = jsonObject.get("step").asString
        val type = jsonObject.get("type").asString
        val action = jsonObject.get("action").asString

        val content = when (type) {
            "button" -> {
                val contentObj = jsonObject.getAsJsonObject("content")
                ContentType.ButtonContent(
                    text = contentObj.get("text").asString,
                    buttons = contentObj.getAsJsonArray("buttons").map {
                        context.deserialize<Button>(it, Button::class.java)
                    }
                )
            }
            "text" -> ContentType.TextContent(jsonObject.get("content").asString)
            "image" -> ContentType.ImageContent(jsonObject.get("content").asString)
            else -> throw JsonParseException("Unknown type: $type")
        }

        return ChatMessageModel(step, type, content, action)
    }
}