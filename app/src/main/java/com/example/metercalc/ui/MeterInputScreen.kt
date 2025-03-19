package com.example.metercalc.ui.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metercalc.data.models.MeterType
import com.example.metercalc.viewmodel.MeterViewModel
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Composable
fun MeterInputScreen(viewModel: MeterViewModel = viewModel()) {
    var selectedMeterType by remember { mutableStateOf(MeterType.ELECTRICITY) }
    var readingValue by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter Meter Reading", style = MaterialTheme.typography.h5)

        // Dropdown for Meter Type Selection
        var expanded by remember { mutableStateOf(false) }
        Box {
            Button(onClick = { expanded = true }) {
                Text(selectedMeterType.name.replace("_", " "))  // Display formatted name
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                MeterType.values().forEach { meter ->
                    DropdownMenuItem(onClick = {
                        selectedMeterType = meter
                        expanded = false
                    }) {
                        Text(meter.name.replace("_", " "))  // Show formatted name
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input Field for Meter Reading
        OutlinedTextField(
            value = readingValue,
            onValueChange = { readingValue = it },
            label = { Text("Meter Reading") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Date Picker Button
        val datePicker = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        )

        Button(onClick = { datePicker.show() }) {
            Text("Select Date: $selectedDate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save Button
        Button(onClick = {
            val value = readingValue.toBigDecimalOrNull()
            if (value != null) {
                viewModel.addReading(selectedMeterType, value, selectedDate)
                Toast.makeText(context, "Reading saved!", Toast.LENGTH_SHORT).show()
                readingValue = "" // Reset input field
            } else {
                Toast.makeText(context, "Enter a valid number", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Save Reading")
        }
    }
}
