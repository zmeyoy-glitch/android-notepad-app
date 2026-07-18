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
 * Экран просмотра заметки с отображением временного слота и статуса
 */
@Composable
fun NoteViewScreen(
    note: NoteEntity, // Конкретная заметка для просмотра
    onBackClick: () -> Unit,
    onEditClick: (NoteEntity) -> Unit,
    onDeleteClick: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(note.title ?: "Без названия", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    // Кнопка редактирования (если заметка не в режиме просмотра)
                    if (!isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Редактировать")
                        }
                        
                        IconButton(onClick = onDeleteClick) {
                            Icon(Icons.Default.Delete, contentDescription = "Удалить")
                        }
                    } else {
                        // Кнопка сохранения при редактировании
                        IconButton(onClick = { isEditing = false }) {
                            Icon(Icons.Default.Check, contentDescription = "Сохранить")
                        }
                    }
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
            // Поле контента заметки (только для чтения в режиме просмотра)
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = note.content ?: "Пусто",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            // Временной слот (только для чтения)
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Временной слот", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Отображение выбранного временного слота
                    val timeSlotText = when (note.startTime) {
                        1 -> "17:00 - 17:15"
                        2 -> "17:15 - 17:30"
                        3 -> "17:30 - 17:45"
                        4 -> "17:45 - 18:00"
                        5 -> "18:00 - 18:15"
                        6 -> "18:15 - 18:30"
                        7 -> "18:30 - 18:45"
                        8 -> "18:45 - 19:00"
                        9 -> "19:00 - 19:15"
                        10 -> "19:15 - 19:30"
                        11 -> "19:30 - 19:45"
                        12 -> "19:45 - 20:00"
                        else -> "Не выбрано время"
                    }
                    
                    Text(
                        text = timeSlotText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF6200EE), // Акцентный цвет
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Статус заметки (только для чтения)
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Статус", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Отображение статуса заметки
                    val statusText = when (note.status) {
                        0 -> "Черновик"
                        1 -> "Опубликовано"
                        else -> "Архив"
                    }
                    
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = when (note.status) {
                            0 -> Color.Gray // Черновик - серый
                            1 -> Color.Green // Опубликовано - зеленый
                            else -> Color.Orange // Архив - оранжевый
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Кнопка редактирования (если заметка не в режиме просмотра)
            if (!isEditing) {
                Button(
                    onClick = { isEditing = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text("Редактировать")
                }
            } else {
                // Кнопка сохранения при редактировании
                Button(
                    onClick = { isEditing = false },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                ) {
                    Text("Сохранить изменения")
                }
            }
        }
    }
}

/**
 * Компонент пустого состояния для просмотра заметки
 */
@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Заметка не найдена", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Нажмите «Назад», чтобы вернуться к списку", 
             style = MaterialTheme.typography.bodySmall,
             color = Color.Gray)
    }
}