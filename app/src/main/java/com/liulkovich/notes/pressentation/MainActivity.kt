package com.liulkovich.notes.pressentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.liulkovich.notes.pressentation.screens.creation.CreateNoteScreen
import com.liulkovich.notes.pressentation.screens.editing.EditNoteScreen
import com.liulkovich.notes.pressentation.ui.theme.NotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesTheme {
                EditNoteScreen(
                    noteId = 5,
                    onFinished = {
                        Log.d("CreateNoteScreen", "Finished")
                  }
                )
//                CreateNoteScreen(
//                    onFinished = {
//                        Log.d("CreateNoteScreen", "Finished")
//                    }
//                )
//                NotesScreen(
//                    onNoteClick = {
//                        Log.d("MainActivity", "onNoteClick: $it")
//                    },
//                    onAddNoteClick = {
//                        Log.d("MainActivity", "onEditNoteClick:")
//                    }
//                )
            }
        }
    }
}
