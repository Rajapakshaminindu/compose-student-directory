package com.example.activity_03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.activity_03.ui.StudentDirectoryScreen
import com.example.activity_03.ui.theme.StudentDirectoryTheme

/**
 * Entry point of the Student Directory application.
 *
 * Responsibilities:
 * - Enables edge-to-edge display for a modern, immersive look.
 * - Sets up the [StudentDirectoryTheme] which provides Material 3 colors and typography.
 * - Hosts the root [StudentDirectoryScreen] composable.
 *
 * The [StudentViewModel] is created lazily by Compose's `viewModel()` call inside
 * [StudentDirectoryScreen] and is scoped to this Activity's lifecycle.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Draw content behind system bars (status bar + navigation bar).
        enableEdgeToEdge()

        setContent {
            StudentDirectoryTheme {
                // A full-screen surface that adapts to the current theme's background color.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StudentDirectoryScreen()
                }
            }
        }
    }
}
