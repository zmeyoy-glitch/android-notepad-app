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
 * Экран просмотра и редактирования существующей заметки
 */
@Composable
fun NoteViewScreen(
    note: NoteEntity?,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onBack: () -> Unit
) {
    if (note == null) {
        EmptyState()
        return
    }
    
    val timeSlotText = when (note.startTime) {
        1L -> "17:00 - 17:15"
        2L -> "17:15 - 17:30"
        3L -> "17:30 - 17:45"
        4L -> "17:45 - 18:00"
        5L -> "18:00 - 18:15"
        6L -> "18:15 - 18:30"
        7L -> "18:30 - 18:45"
        8L -> "18:45 - 19:00"
        9L -> "19:00 - 19:15"
        10L -> "19:15 - 19:30"
        11L -> "19:30 - 19:45"
        12L -> "19:45 - 20:00"
        else -> "Не выбрано время"
    }
    
    val statusText = when (note.status) {
        0 -> "Черновик"
        1 -> "Опубликовано"
        else -> "Архив"
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(note.title ?: "Без названия") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Информация о временном слоте и статусе
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Временной слот", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(timeSlotText, style = MaterialTheme.typography.bodyLarge)
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text("Статус", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(statusText, style = MaterialTheme.typography.bodyLarge)
                }
            }
            
            // Контент заметки
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Контент", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    val contentText = note.content ?: "Пусто"
                    Text(contentText, style = MaterialTheme.typography.bodyLarge)
                }
            }
            
            // Кнопки действий
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onEditClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text("Редактировать")
                }
                
                Button(
                    onClick = onDeleteClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Удалить")
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