package com.cemreonur.n11chatbot.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.cemreonur.n11chatbot.R
import com.cemreonur.n11chatbot.data.ChatItem
import com.cemreonur.n11chatbot.data.domain.ContentType
import com.cemreonur.n11chatbot.ui.theme.CardBackground

@Composable
fun ChatBubble(
    data: ChatItem,
    onUserChoiceSelected: (String, String) -> Unit,
    startConversation: () -> Unit
) {
    when (data) {
        /* is ChatItem.UserMessage -> {
             Text(
                 text = data.message.text,
                 color = Color.White,
                 modifier = Modifier
                     .background(Color.Blue, shape = RoundedCornerShape(8.dp))
                     .padding(8.dp)
             )
         }*/

        is ChatItem.StepMessage -> {
            when (val content = data.step.content) {
                is ContentType.ButtonContent -> {

                    Column {
                        Row {
                            Box(
                                modifier = Modifier
                                    .size(width = 50.dp, height = 40.dp)
                                    .padding(end = 10.dp)
                                    .clip(shape = RoundedCornerShape(20.dp))
                                    .background(CardBackground)
                            ) {
                                Image(
                                    modifier = Modifier.size(40.dp),
                                    painter = painterResource(R.drawable.n11),
                                    contentDescription = null
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(
                                        CardBackground
                                    )
                                    .padding(10.dp)
                            ) {

                                Text(
                                    text = content.text,
                                    modifier = Modifier.padding(8.dp),
                                    fontSize = 15.sp
                                )
                            }
                        }

                        content.buttons.forEach { button ->
                            Box(
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .height(45.dp)
                                    .clip(shape = RoundedCornerShape(20.dp))
                                    .background(
                                        CardBackground
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .padding(10.dp)
                                    .align(Alignment.End)
                                    .alpha(
                                        if (data.answeredStepName.contains(button.action) || data.answeredStepName.isEmpty()) {
                                            1.0f
                                        } else {
                                            0.2f
                                        }

                                    )
                                    .clickable {
                                        //TODO: Henüz bir seçim yapılmamış demektir.
                                        if (data.answeredStepName.isEmpty()) {
                                            onUserChoiceSelected(data.step.step, button.action)
                                        }
                                    }, contentAlignment = Alignment.CenterEnd
                            ) {
                                Row {
                                    if (data.answeredStepName.contains(button.action)) {
                                        Icon(
                                            modifier = Modifier.padding(end = 10.dp),
                                            imageVector = Icons.Default.Done,
                                            contentDescription = "Check",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Text(
                                        text = button.label,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                    }
                }

                is ContentType.ImageContent -> {
                    //TODO: Resim içeriği olmadığı gözükmüyor
                    AsyncImage(
                        model = content.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                            .height(150.dp),
                        placeholder = painterResource(R.drawable.ic_launcher_background)
                    )
                }

                is ContentType.TextContent ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .background(
                                CardBackground
                            )
                            .padding(10.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = content.text,
                                modifier = Modifier.padding(8.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "Konuşma sonlandırıldı.\nBizi tercih ettiğiniz için teşekkür ederiz.",
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.tertiary,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        startConversation()
                                    }, horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Restart Conversation",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    "Tekrar Başlat", fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            }
                        }
                    }

            }
        }
    }
}
