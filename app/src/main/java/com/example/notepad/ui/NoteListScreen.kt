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
 * Экран списка заметок с отображением временных слотов и статусов
 */
@Composable
fun NoteListScreen(
    notes: List<NoteEntity>, // Список всех заметок
    onBackClick: () -> Unit,
    onViewClick: (NoteEntity) -> Unit,
    onEditClick: (NoteEntity) -> Unit,
    onDeleteClick: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои заметки") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Список заметок
            if (notes.isEmpty()) {
                EmptyState()
            } else {
                notes.forEach { note ->
                    NoteCard(
                        note = note,
                        onClick = { onViewClick(note) },
                        onEditClick = { onEditClick(note) },
                        onDeleteClick = { onDeleteClick(note.id) }
                    )
                }
            }
        }
    }
}

/**
 * Карточка заметки с отображением временного слота и статуса
 */
@Composable
fun NoteCard(
    note: NoteEntity,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Заголовок заметки
            Text(
                text = note.title ?: "Без названия",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Краткий контент (первые 100 символов)
            val previewText = note.content?.take(100) ?: "Пусто"
            Text(
                text = previewText,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Временной слот (только для чтения)
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
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF6200EE), // Акцентный цвет для времени
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Статус заметки (только для чтения)
            val statusText = when (note.status) {
                0 -> "Черновик"
                1 -> "Опубликовано"
                else -> "Архив"
            }
            
            Text(
                text = statusText,
                style = MaterialTheme.typography.bodySmall,
                color = when (note.status) {
                    0 -> Color.Gray // Черновик - серый
                    1 -> Color.Green // Опубликовано - зеленый
                    else -> Color.Orange // Архив - оранжевый
                }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Кнопки действий (редактировать и удалить)
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
 * Компонент пустого состояния для списка заметок
 */
@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Нет заметок", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Нажмите «Назад», чтобы вернуться к главному экрану", 
             style = MaterialTheme.typography.bodySmall,
             color = Color.Gray)
    }
}