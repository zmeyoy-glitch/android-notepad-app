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
 * Экран редактора заметки с выбором временных слотов и статусов
 */
@Composable
fun NoteEditorScreen(
    note: NoteEntity?, // null - создание новой, не null - редактирование существующей
    onBackClick: () -> Unit,
    onSaveClick: (title: String?, content: String, startTime: Long, status: Int) -> Unit,
    onDeleteClick: () -> Unit
) {
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var selectedTimeSlot by remember { 
        mutableStateOf(
            note?.startTime ?: 1L // По умолчанию первый слот (17:00-17:15)
        ) 
    }
    var selectedStatus by remember { 
        mutableStateOf(
            note?.status ?: 0 // По умолчанию черновик
        ) 
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (note == null) "Новая заметка" else "Редактирование") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Поле ввода заголовка
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Заголовок", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Введите заголовок...") },
                        maxLines = 2
                    )
                }
            }
            
            // Поле ввода контента
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Контент", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Введите текст заметки...") },
                        maxLines = 10,
                        minLines = 5
                    )
                }
            }
            
            // Выбор временного слота (17:00 - 20:00 с шагом 15 минут)
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Временной слот", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // RadioButtons для выбора временного слота
                    val timeSlots = listOf(
                        1L to "17:00 - 17:15",
                        2L to "17:15 - 17:30",
                        3L to "17:30 - 17:45",
                        4L to "17:45 - 18:00",
                        5L to "18:00 - 18:15",
                        6L to "18:15 - 18:30",
                        7L to "18:30 - 18:45",
                        8L to "18:45 - 19:00",
                        9L to "19:00 - 19:15",
                        10L to "19:15 - 19:30",
                        11L to "19:30 - 19:45",
                        12L to "19:45 - 20:00"
                    )
                    
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        timeSlots.forEach { (slotId, slotText) ->
                            RadioButton(
                                selected = selectedTimeSlot == slotId,
                                onClick = { selectedTimeSlot = slotId },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF6200EE))
                            )
                            Text(slotText, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
            
            // Выбор статуса заметки
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Статус", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // RadioButtons для выбора статуса
                    val statuses = listOf(
                        0 to "Черновик (не опубликовано)",
                        1 to "Опубликовано"
                    )
                    
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        statuses.forEach { (statusId, statusText) ->
                            RadioButton(
                                selected = selectedStatus == statusId.toLong(),
                                onClick = { selectedStatus = statusId.toLong() },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF6200EE))
                            )
                            Text(statusText, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
            
            // Кнопки действий
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { 
                        onSaveClick(title, content, selectedTimeSlot, selectedStatus.toInt())
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text("Сохранить")
                }
                
                if (note != null) {
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
}

/**
 * Компонент пустого состояния для редактора заметки
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