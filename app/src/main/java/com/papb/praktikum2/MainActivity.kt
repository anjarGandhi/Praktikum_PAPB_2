package com.papb.praktikum2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.papb.praktikum2.ui.theme.Praktikum2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Praktikum2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyScreen()
                }
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Praktikum 4",
            color = Color.Black,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
fun MyScreen() {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    var inputText by remember { mutableStateOf("") }
    var numberInput by remember { mutableStateOf("") }
    val isFormFilled = inputText.isNotBlank() && numberInput.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HeaderSection()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Icon Profile",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(65.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    label = { Text("Masukan Nama anda") },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            TextField(
                value = numberInput,
                onValueChange = {
                    if (it.length <= 15 && it.all { char -> char.isDigit() }) {
                        numberInput = it
                    }
                },
                label = { Text("Masukan NIM anda") },
                modifier = Modifier.weight(1f)
            )
        }

        Button(
            onClick = {
                text = "$inputText, $numberInput"
            },
            enabled = isFormFilled,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormFilled) MaterialTheme.colorScheme.primary else Color.Gray
            ),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            // Box inside the button that handles long-press gesture
            Box(
                modifier = Modifier
                    .padding(top = 3.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                Toast.makeText(context, "Nama: $inputText, NIM: $numberInput", Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("Submit")
            }
        }



        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Praktikum2Theme {
        MyScreen()
    }
}
