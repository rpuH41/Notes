package com.liulkovich.notes.pressentation.screens.notes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.liulkovich.notes.domain.Note

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = viewModel(),
) {

    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier
            .padding(top = 48.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.pinnedNotes.forEach { note ->
                    item {
                        NotesCard(
                            note = note,
                            onNoteClick = {
                                viewModel.processCommand(NotesCommand.SwitchPinnedStatus(it.id))
                            }
                        )
                    }
                }
            }
        }
        items(state.otherNotes) { note ->
            NotesCard(
                note = note,
                onNoteClick = {
                    viewModel.processCommand(NotesCommand.SwitchPinnedStatus(it.id))
                }
            )
        }
    }
}

@Composable
fun NotesCard(
    modifier: Modifier = Modifier,
    note: Note,
    onNoteClick: (Note) -> Unit,
) {
    Text(
        modifier = Modifier.clickable {
            onNoteClick(note)
        },
        text = "${note.title} - ${note.content}",
        fontSize = 24.sp,
    )
}