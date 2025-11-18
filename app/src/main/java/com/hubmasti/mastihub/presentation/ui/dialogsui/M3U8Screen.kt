package com.hubmasti.mastihub.presentation.ui.dialogsui

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun M3U8Screen(
    initialUrl: String = "",
    onNext: (String) -> Unit = {}
) {

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var url by rememberSaveable { mutableStateOf(initialUrl) }
    var showError by rememberSaveable { mutableStateOf(false) }

    // Optional: auto-fill from clipboard if it contains .m3u8
    LaunchedEffect(Unit) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = clipboard.primaryClip
        val pasted = clip?.getItemAt(0)?.text?.toString()?.trim()
        if (!pasted.isNullOrEmpty() && pasted.endsWith(".m3u8")) {
            url = pasted
        }
    }

    // Button enable condition: valid .m3u8 URL
    val isButtonEnabled = isM3u8(url)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = url,
            onValueChange = {
                url = it
                if (showError) showError = false
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter Url M3U8 Wala") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if (isButtonEnabled) {
                        onNext(url)
                    } else {
                        showError = true
                    }
                }
            )
        )

        if (showError) {
            Text(
                text = "Please enter a valid .m3u8 URL",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 6.dp, start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                onNext(url)
            },
            enabled = isButtonEnabled, // <-- disabled if invalid
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Next")
        }
    }
}

fun isM3u8(text: String): Boolean {
    val trimmed = text.trim()
    if (trimmed.isEmpty()) return false
    val beforeQuery = trimmed.substringBefore('?')
    return beforeQuery.lowercase().endsWith(".m3u8")
}
