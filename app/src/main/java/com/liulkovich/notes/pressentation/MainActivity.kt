package com.liulkovich.notes.pressentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.liulkovich.notes.pressentation.navigation.CustomNavGraph
import com.liulkovich.notes.pressentation.navigation.NavGraph
import com.liulkovich.notes.pressentation.ui.theme.NotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesTheme {
                NavGraph()
            }
        }
    }
}
