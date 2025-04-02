package com.cemreonur.n11chatbot.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cemreonur.n11chatbot.data.ChatRepository
import com.cemreonur.n11chatbot.data.domain.ChatMessageModel
import com.cemreonur.n11chatbot.data.domain.ChatUseCase
import com.cemreonur.n11chatbot.data.local.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val chatUseCase: ChatUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val messages: StateFlow<List<ChatMessage>> = repository.getAllMessages()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val chatSteps: StateFlow<List<ChatMessageModel>> = chatUseCase.chatSteps
    private var _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private var _isConversationEnd = MutableStateFlow(false)
    val isConversationEnd: StateFlow<Boolean> = _isConversationEnd


    init {
        loadChatSteps()
    }

    fun toggleDialog(isShow: Boolean) {
        _showDialog.update { isShow }
    }

    fun endUIConversation(){
        _isConversationEnd.update { true }
    }

    fun endConversation() {
        viewModelScope.launch {
            try {
                _isConversationEnd.update { true }
                repository.closeWebSocket()
                repository.clearChatHistory()
                repository.startListeningToWebSocket()
                loadChatSteps()
                _isConversationEnd.update { false }
                Log.d("ChatViewModel", "Database removed and conversation start again.")
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error: ${e.message}")
            }
        }
    }


    fun getConvertedItem(string: String): ChatMessageModel = repository.convertItem(string)

    private fun loadChatSteps() {
        viewModelScope.launch {
            chatUseCase.loadChatFlow(context)
            val existingMessages = repository.getAllMessages().first()
            if (existingMessages.isEmpty()) {
                val firstStep = chatSteps.firstOrNull()
                firstStep?.let {
                    repository.insertMessage(
                        chatMessageModel = it.first()
                    )
                }
            }
        }
    }

    fun onUserChoiceSelected(currentStep: String, action: String) {
        viewModelScope.launch {
            chatUseCase.processUserChoice(currentStep = currentStep, action)
        }
    }
}
