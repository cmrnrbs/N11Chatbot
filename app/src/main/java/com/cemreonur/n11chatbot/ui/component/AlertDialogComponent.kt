package com.cemreonur.n11chatbot.ui.component

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.cemreonur.n11chatbot.ui.theme.CardBackground

@Composable
fun AlertDialogComponent(
    title: String,
    description: String,
    onOkay: () -> Unit,
    onDismiss: () -> Unit,
    context: Context
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .background(CardBackground)
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = title, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = description, fontSize = 15.sp, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Button(onClick = {
                        onOkay()
                        onDismiss()
                        Toast.makeText(context, "Yeni Sohbet Başlatıldı", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Evet")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        onClick = {
                            onDismiss()
                        }) {
                        Text("Hayır")
                    }
                }
            }
        }
    }
}