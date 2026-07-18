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
 * Экран редактора заметки с выбором временного слота и статуса
 */
@Composable
fun NoteEditorScreen(
    note: NoteEntity?, // Существующая заметка для редактирования или null для новой
    onBackClick: () -> Unit,
    onSaveClick: (title: String?, content: String, startTime: Long, status: Int) -> Unit
) {
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var startTime by remember { mutableStateOf(note?.startTime ?: 1L) } // По умолчанию 17:00-17:15
    var status by remember { mutableStateOf(note?.status ?: 0) } // По умолчанию черновик
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактирование заметки") },
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
            // Поле заголовка заметки
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Заголовок", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Введите заголовок") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                }
            }
            
            // Поле контента заметки
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Контент", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Введите контент заметки") },
                        minLines = 5,
                        maxLines = 20,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                }
            }
            
            // Выбор временного слота (17:00-20:00 с шагом 15 минут)
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Временной слот", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Список временных слотов с выбором
                    val timeSlots = listOf(
                        1 to "17:00 - 17:15",
                        2 to "17:15 - 17:30",
                        3 to "17:30 - 17:45",
                        4 to "17:45 - 18:00",
                        5 to "18:00 - 18:15",
                        6 to "18:15 - 18:30",
                        7 to "18:30 - 18:45",
                        8 to "18:45 - 19:00",
                        9 to "19:00 - 19:15",
                        10 to "19:15 - 19:30",
                        11 to "19:30 - 19:45",
                        12 to "19:45 - 20:00"
                    )
                    
                    timeSlots.forEachIndexed { index, (value, text) ->
                        RadioButton(
                            selected = startTime == value.toLong(),
                            onClick = { startTime = value.toLong() },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
            
            // Выбор статуса заметки
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Статус", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Список статусов с выбором
                    val statuses = listOf(
                        0 to "Черновик" to Color.Gray,
                        1 to "Опубликовано" to Color.Green
                    )
                    
                    statuses.forEachIndexed { index, (value, text, color) ->
                        RadioButton(
                            selected = status == value.toLong(),
                            onClick = { status = value.toLong() },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = color,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
            
            // Кнопка сохранения
            Button(
                onClick = { 
                    onSaveClick(title.ifEmpty { "Без названия" }, content, startTime, status)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Сохранить")
            }
        }
    }
}

/**
 * Компонент выбора временного слота с отображением выбранного времени
 */
@Composable
fun TimeSlotSelector(
    selectedTime: Long,
    onTimeSelected: (Long) -> Unit
) {
    val timeSlots = listOf(
        1 to "17:00 - 17:15",
        2 to "17:15 - 17:30",
        3 to "17:30 - 17:45",
        4 to "17:45 - 18:00",
        5 to "18:00 - 18:15",
        6 to "18:15 - 18:30",
        7 to "18:30 - 18:45",
        8 to "18:45 - 19:00",
        9 to "19:00 - 19:15",
        10 to "19:15 - 19:30",
        11 to "19:30 - 19:45",
        12 to "19:45 - 20:00"
    )
    
    Column {
        Text("Выберите временной слот:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        
        Spacer(modifier = Modifier.height(8.dp))
        
        timeSlots.forEachIndexed { index, (value, text) ->
            RadioButton(
                selected = selectedTime == value.toLong(),
                onClick = { onTimeSelected(value.toLong()) },
                modifier = Modifier.fillMaxWidth()
            )
            
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

/**
 * Компонент выбора статуса заметки с отображением выбранного статуса
 */
@Composable
fun StatusSelector(
    selectedStatus: Int,
    onStatusSelected: (Int) -> Unit
) {
    val statuses = listOf(
        0 to "Черновик" to Color.Gray,
        1 to "Опубликовано" to Color.Green
    )
    
    Column {
        Text("Выберите статус:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        
        Spacer(modifier = Modifier.height(8.dp))
        
        statuses.forEachIndexed { index, (value, text, color) ->
            RadioButton(
                selected = selectedStatus == value.toLong(),
                onClick = { onStatusSelected(value.toLong()) },
                modifier = Modifier.fillMaxWidth()
            )
            
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = color,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}