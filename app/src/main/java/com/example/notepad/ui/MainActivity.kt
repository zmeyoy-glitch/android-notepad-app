package com.example.notepad.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.notepad.data.NoteEntity

/**
 * Главный экран приложения с навигацией между списком заметок и редактором/просмотром
 */
@Composable
fun MainActivity(
    notes: List<NoteEntity>, // Список всех заметок
    onBackClick: () -> Unit,
    onEditClick: (NoteEntity) -> Unit,
    onDeleteClick: (Long) -> Unit,
    onSaveClick: (title: String?, content: String, startTime: Long, status: Int) -> Unit
) {
    var currentScreen by remember { mutableStateOf(ScreenType.LIST) } // LIST или EDITOR
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        when (currentScreen) {
                            ScreenType.LIST -> "Мои заметки"
                            else -> "Редактирование"
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            when (currentScreen) {
                ScreenType.LIST -> {
                    // Список заметок
                    NoteListScreen(
                        notes = notes,
                        onBackClick = onBackClick,
                        onViewClick = { note -> 
                            currentScreen = ScreenType.VIEW 
                            // Передаем заметку для просмотра (можно добавить отдельный экран)
                        },
                        onEditClick = { note -> 
                            currentScreen = ScreenType.EDITOR
                            // Передаем заметку для редактирования
                        },
                        onDeleteClick = onDeleteClick
                    )
                }
                
                else -> {
                    // Редактор или просмотр заметки (можно добавить отдельный экран)
                    NoteEditorScreen(
                        note = null, // По умолчанию новая заметка
                        onBackClick = { currentScreen = ScreenType.LIST },
                        onSaveClick = { title, content, startTime, status -> 
                            onSaveClick(title, content, startTime, status)
                            currentScreen = ScreenType.LIST
                        }
                    )
                }
            }
        }
    }
}

/**
 * Перечисление типов экранов для управления навигацией
 */
enum class ScreenType {
    LIST, // Список заметок
    EDITOR, // Редактор заметки
    VIEW   // Просмотр заметки (можно добавить позже)
}