package com.cemreonur.n11chatbot.data.util

import com.cemreonur.n11chatbot.data.domain.ContentType
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class ContentSerializer : JsonSerializer<ContentType> {
    override fun serialize(
        src: ContentType?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
       return when(src){
           is ContentType.ButtonContent -> context!!.serialize(src)
           is ContentType.ImageContent -> JsonPrimitive(src.imageUrl)
           is ContentType.TextContent -> JsonPrimitive(src.text)
           else -> throw JsonParseException("")
       }
    }
}
