package com.pclandconverter.pattachittalandconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pattachittalandconverter.ui.theme.PattaChittaLandConverterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PattaChittaLandConverterTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text(
                                text = " ",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                            )
                        }
                    }
                ) { innerPadding ->
                    LandConverterApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun LandConverterApp(modifier: Modifier = Modifier) {
    var hectares by remember { mutableStateOf("") }
    var ares by remember { mutableStateOf("") }
    var acres by remember { mutableStateOf("") }
    var cents by remember { mutableStateOf("") }
    var squareFeet by remember { mutableStateOf("") }

    // Obtain keyboard controller instance
    val keyboardController = LocalSoftwareKeyboardController.current

    fun convertLandArea() {
        val hectaresValue = hectares.toDoubleOrNull() ?: 0.0
        val aresValue = ares.toDoubleOrNull() ?: 0.0

        val hectaresToAcres = 2.47105
        val aresToAcres = 0.0247105
        val acresToCents = 100.0
        val acresToSquareFeet = 43560.0

        val totalAcres = (hectaresValue * hectaresToAcres) + (aresValue * aresToAcres)
        val totalCents = totalAcres * acresToCents
        val totalSquareFeet = totalAcres * acresToSquareFeet

        acres = String.format("Acres: %.4f", totalAcres)
        cents = String.format("Cents: %.4f", totalCents)
        squareFeet = String.format("Square Feet: %.2f", totalSquareFeet)

        // Hide the keyboard after conversion
        keyboardController?.hide()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = hectares,
            onValueChange = { value ->
                // Ensure only numeric or decimal input
                if (value.isEmpty() || value.toDoubleOrNull() != null || value == ".") {
                    hectares = value
                }
            },
            label = { Text("Enter hectares") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    // Move focus to the next field (ares)
                    keyboardController?.hide()
                }
            )
        )
        OutlinedTextField(
            value = ares,
            onValueChange = { value ->
                // Ensure only numeric or decimal input
                if (value.isEmpty() || value.toDoubleOrNull() != null || value == ".") {
                    ares = value
                }
            },
            label = { Text("Enter ares") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Trigger conversion when done button pressed
                    convertLandArea()
                }
            )
        )
        Button(onClick = { convertLandArea() }) {
            Text("Convert")
        }
        Text(text = acres)
        Text(text = cents)
        Text(text = squareFeet)
    }
}

@Preview(showBackground = true)
@Composable
fun LandConverterAppPreview() {
    PattaChittaLandConverterTheme {
        LandConverterApp()
    }
}
