package com.cemreonur.n11chatbot.ui.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cemreonur.n11chatbot.R
import com.cemreonur.n11chatbot.data.ChatItem
import com.cemreonur.n11chatbot.ui.component.AlertDialogComponent
import com.cemreonur.n11chatbot.ui.theme.CardBackground
import com.cemreonur.n11chatbot.ui.viewmodel.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreen(viewModel: ChatViewModel = hiltViewModel()) {
    val messages by viewModel.messages.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val isConversationEnd by viewModel.isConversationEnd.collectAsState()
    val chatItems = (messages.map {
        /*if (it.isUser) {
            ChatItem.UserMessage(it)
        } else {
            val data = viewModel.getConvertedItem(it.text)
            ChatItem.StepMessage(data)
        }*/
        val data = viewModel.getConvertedItem(it.text)
        if ("end_conversation" == data.action) {
            viewModel.endUIConversation()
        }
        ChatItem.StepMessage(it.answeredStepName, data)
    }).sortedBy { it.step.step }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = CardBackground,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Image(
                            modifier = Modifier.padding(
                                start = (if (isConversationEnd) {
                                    0.dp
                                } else {
                                    40.dp
                                })
                            ),
                            painter = painterResource(R.drawable.n11_logo),
                            contentDescription = null,
                            alignment = Alignment.Center,
                        )
                    }
                },
                actions = {
                    if (!isConversationEnd) {
                        IconButton(onClick = {
                            viewModel.toggleDialog(true)
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "End of Conversation",
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(10.dp)
        ) {

            LazyColumn(
                reverseLayout = false
            ) {
                items(chatItems) { chatItem ->
                    Box(modifier = Modifier.padding(bottom = 10.dp)) {
                        ChatBubble(
                            chatItem,
                            onUserChoiceSelected = { stepName, action ->
                                viewModel.onUserChoiceSelected(
                                    stepName,
                                    action
                                )
                            }, startConversation = {
                                viewModel.endConversation()
                            })
                    }
                }
            }

            if (showDialog) {
                AlertDialogComponent(
                    title = "Uyarı!",
                    description = "Görüşmeyi sonlandırmak ve yeni bir sohbet başlatmak istediğinize emin misiniz?",
                    onOkay = {
                        viewModel.endConversation()
                    },
                    onDismiss = { viewModel.toggleDialog(false) },
                    LocalContext.current
                )
            }
        }
    }
}
