package com.example.metercalc.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import com.example.metercalc.ui.screens.MeterInputScreen
import com.example.metercalc.ui.screens.ReadingsTableScreen
import com.example.metercalc.ui.screens.TotalCostsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    var currentScreen by remember { mutableStateOf("input") }

                    when (currentScreen) {
                        "input" -> MeterInputScreen(
                            onNavigateToReadingsTable = { currentScreen = "table" },
                            onNavigateToTotalCosts = { currentScreen = "costs" }
                        )
                        "table" -> ReadingsTableScreen(
                            onNavigateBack = { currentScreen = "input" }
                        )
                        "costs" -> TotalCostsScreen(
                            onNavigateBack = { currentScreen = "input" }
                        )
                    }
                }
            }
        }
    }
}