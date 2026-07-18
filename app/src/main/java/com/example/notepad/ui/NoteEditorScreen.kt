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
    note: NoteEntity?,
    onBackClick: () -> Unit,
    onSaveClick: (NoteEntity) -> Unit,
    onUpdateStartTime: (Long) -> Unit,
    onUpdateStatus: (Int) -> Unit
) {
    // Текущее состояние заметки в редакторе
    var editorNote by remember { mutableStateOf(note ?: NoteEntity(0L, "Без названия", "", 1, 0)) }
    
    // Выбор временного слота (1-12, соответствует 17:00-20:00 с шагом 15 минут)
    var selectedTimeSlot by remember { mutableStateOf(editorNote.startTime) }
    
    // Выбор статуса заметки (0 - черновик, 1 - опубликовано)
    var selectedStatus by remember { mutableStateOf(editorNote.status) }
    
    // Заголовок заметки
    var titleText by remember { mutableStateOf(editorNote.title.ifEmpty { "Без названия" }) }
    
    // Контент заметки
    var contentText by remember { mutableStateOf(editorNote.content) }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Верхняя панель с кнопкой назад и заголовком
        TopAppBar(
            title = { 
                Text(
                    text = if (note?.id != null && note?.id > 0) "Редактирование заметки" else "Новая заметка",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Кнопка назад
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = if (note?.id != null && note?.id > 0) "Редактирование заметки" else "Новая заметка",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Выбор временного слота
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "⏰ Временной слот",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Выбор временного слота (1-12)
                Row {
                    TimeSlotPicker(
                        selectedTimeSlot = selectedTimeSlot,
                        onTimeSlotSelected = { newSlot ->
                            selectedTimeSlot = newSlot
                            onUpdateStartTime(newSlot)
                        }
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // Отображение выбранного времени в читаемом формате
                    Text(
                        text = "${NoteHelpers.getTimeSlotText(selectedTimeSlot)}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color.Blue
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Выбор статуса заметки
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📌 Статус заметки",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = NoteHelpers.getStatusColor(selectedStatus)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Выбор статуса (черновик/опубликовано)
                Row {
                    StatusPicker(
                        selectedStatus = selectedStatus,
                        onStatusSelected = { newStatus ->
                            selectedStatus = newStatus
                            onUpdateStatus(newStatus)
                        }
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // Отображение выбранного статуса в читаемом формате
                    Text(
                        text = "${NoteHelpers.getStatusText(selectedStatus)}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = NoteHelpers.getStatusColor(selectedStatus)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Поле заголовка заметки
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📝 Заголовок",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = titleText,
                    onValueChange = { 
                        titleText = it 
                        editorNote = editorNote.copy(title = it) 
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Введите заголовок заметки...") },
                    maxLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Blue,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Поле контента заметки
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📄 Контент",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = contentText,
                    onValueChange = { 
                        contentText = it 
                        editorNote = editorNote.copy(content = it) 
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Введите контент заметки...") },
                    maxLines = 10,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Blue,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Кнопка сохранения заметки
        Button(
            onClick = { 
                onSaveClick(editorNote) 
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
            enabled = titleText.isNotEmpty() || contentText.isNotEmpty()
        ) {
            Text("Сохранить заметку")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Компонент выбора временного слота (1-12, соответствует 17:00-20:00 с шагом 15 минут)
 */
@Composable
fun TimeSlotPicker(
    selectedTimeSlot: Long,
    onTimeSlotSelected: (Long) -> Unit
) {
    // Генерация кнопок для выбора временных слотов (1-12)
    val timeSlots = listOf(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L)
    
    Row {
        timeSlots.forEachIndexed { index, slot ->
            val isSelected = selectedTimeSlot == slot
            
            // Отображение номера слота и времени в читаемом формате
            TextButton(
                onClick = { 
                    onTimeSlotSelected(slot) 
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (isSelected) Color.Blue else Color.Black,
                    disabledContentColor = Color.Gray
                )
            ) {
                Text(
                    text = "${slot} (${NoteHelpers.getTimeSlotText(slot)})"
                )
            }
        }
    }
}

/**
 * Компонент выбора статуса заметки (черновик/опубликовано)
 */
@Composable
fun StatusPicker(
    selectedStatus: Int,
    onStatusSelected: (Int) -> Unit
) {
    // Генерация кнопок для выбора статусов (0 - черновик, 1 - опубликовано)
    val statuses = listOf(0 to "Черновик", 1 to "Опубликовано")
    
    Row {
        statuses.forEachIndexed { index, (statusValue, statusText) ->
            val isSelected = selectedStatus == statusValue
            
            // Отображение статуса в читаемом формате с цветом
            TextButton(
                onClick = { 
                    onStatusSelected(statusValue) 
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (isSelected) NoteHelpers.getStatusColor(statusValue) else Color.Black,
                    disabledContentColor = Color.Gray
                )
            ) {
                Text(textText)
            }
        }
    }
}

/**
 * Компонент отображения выбранного временного слота в читаемом формате
 */
@Composable
fun SelectedTimeSlotDisplay(selectedTimeSlot: Long) {
    val timeText = NoteHelpers.getTimeSlotText(selectedTimeSlot)
    
    Text(
        text = "⏰ $timeText",
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Medium,
        color = Color.Blue
    )
}

/**
 * Компонент отображения выбранного статуса в читаемом формате
 */
@Composable
fun SelectedStatusDisplay(selectedStatus: Int) {
    val statusText = NoteHelpers.getStatusText(selectedStatus)
    val statusColor = NoteHelpers.getStatusColor(selectedStatus)
    
    Text(
        text = "📌 $statusText",
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Medium,
        color = statusColor
    )
}