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
    notes: List<NoteEntity>,
    onBackClick: () -> Unit,
    onViewClick: (NoteEntity) -> Unit,
    onEditClick: (NoteEntity) -> Unit,
    onDeleteClick: (Long) -> Unit
) {
    if (notes.isEmpty()) {
        // Пустой список заметок
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Нет заметок", style = MaterialTheme.typography.headlineMedium)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                "Нажмите на кнопку '+' в правом верхнем углу, чтобы создать новую заметку",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }
    } else {
        // Список заметок с временными слотами и статусами
        Column(modifier = Modifier.fillMaxSize()) {
            // Кнопка добавления новой заметки
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    alignment = Alignment.CenterVertically
                ) {
                    Text("Новая заметка", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // Кнопка добавления новой заметки
                    Button(
                        onClick = { 
                            onEditClick(NoteEntity(0L, "Без названия", "", 1, 0)) 
                        },
                        modifier = Modifier.padding(end = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                    ) {
                        Text("+")
                    }
                }
            }
            
            // Список заметок
            notes.forEachIndexed { index, note ->
                NoteListItemCard(
                    note = note,
                    onViewClick = { 
                        onViewClick(note) 
                    },
                    onEditClick = { 
                        onEditClick(note) 
                    },
                    onDeleteClick = { 
                        onDeleteClick(note.id) 
                    }
                )
                
                // Отступ между карточками
                if (index < notes.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

/**
 * Карточка заметки в списке с отображением временного слота и статуса
 */
@Composable
fun NoteListItemCard(
    note: NoteEntity,
    onViewClick: (NoteEntity) -> Unit,
    onEditClick: (NoteEntity) -> Unit,
    onDeleteClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = { 
            onViewClick(note) // По умолчанию открываем для просмотра
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Заголовок заметки
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Контент заметки (ограниченный по длине)
            Text(
                text = note.content.take(100).ifEmpty { "..." },
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Временной слот и статус заметки
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                alignment = Alignment.CenterVertically
            ) {
                // Временной слот
                Text(
                    text = "${NoteHelpers.getTimeSlotText(note.startTime)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Blue,
                    fontWeight = FontWeight.Medium
                )
                
                // Статус заметки
                val statusColor = NoteHelpers.getStatusColor(note.status)
                Text(
                    text = "${NoteHelpers.getStatusText(note.status)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = statusColor,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Кнопки действий (редактировать и удалить)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                alignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { 
                        onEditClick(note) // Редактирование заметки
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Редактировать")
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Кнопка удаления заметки
                OutlinedButton(
                    onClick = { 
                        onDeleteClick(note.id) // Удаление заметки
                    },
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Удалить")
                }
            }
        }
    }
}

/**
 * Компонент выбора временного слота для отображения в карточке заметки
 */
@Composable
fun TimeSlotDisplay(note: NoteEntity) {
    val timeText = NoteHelpers.getTimeSlotText(note.startTime)
    
    Text(
        text = "⏰ $timeText",
        style = MaterialTheme.typography.bodySmall,
        color = Color.Blue,
        fontWeight = FontWeight.Medium
    )
}

/**
 * Компонент отображения статуса заметки в карточке
 */
@Composable
fun StatusDisplay(note: NoteEntity) {
    val statusText = NoteHelpers.getStatusText(note.status)
    val statusColor = NoteHelpers.getStatusColor(note.status)
    
    Text(
        text = "📌 $statusText",
        style = MaterialTheme.typography.bodySmall,
        color = statusColor,
        fontWeight = FontWeight.Medium
    )
}