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
 * Экран создания и редактирования заметки с выбором временного слота
 */
@Composable
fun NoteEditorScreen(
    note: NoteEntity? = null,
    onSave: (NoteEntity) -> Unit,
    onBack: () -> Unit
) {
    // Состояние формы
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var selectedSlot by remember { 
        mutableStateOf(
            note?.startTime?.let { it.toInt() } ?: 1 // По умолчанию первый слот
        ) 
    }
    
    // Список временных слотов (12 слотов по 15 минут)
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
    
    // Определяем статус заметки (для существующих)
    val status = note?.status ?: 0
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        if (note != null) "Редактировать" else "Новая заметка",
                        fontWeight = FontWeight.Bold
                    ) 
                },
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Выбор временного слота
            TimeSlotSelector(timeSlots, selectedSlot) { newSlot ->
                selectedSlot = newSlot
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Поле ввода заголовка
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Заголовок заметки") },
                placeholder = { Text("Введите заголовок...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Поле ввода контента
            TextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Контент заметки") },
                placeholder = { Text("Введите текст заметки...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5,
                maxLines = 10
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Кнопка сохранения
            Button(
                onClick = { 
                    val newNote = note?.copy(
                        title = title.ifEmpty { null },
                        content = content,
                        startTime = selectedSlot.toLong(),
                        status = status
                    ) ?: NoteEntity(
                        id = 0L, // Будет установлен Room при вставке
                        title = title.ifEmpty { null },
                        content = content,
                        startTime = selectedSlot.toLong(),
                        status = status,
                        createdAt = System.currentTimeMillis()
                    )
                    
                    onSave(newNote)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить")
            }
        }
    }
}

/**
 * Компонент выбора временного слота с цветовой индикацией
 */
@Composable
fun TimeSlotSelector(
    slots: List<Pair<Int, String>>,
    selectedSlot: Int,
    onSlotSelected: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Выберите временной слот", style = MaterialTheme.typography.titleMedium)
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Сетка из 4 строк по 3 слота в каждой (12 слотов всего)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(slots.size) { index ->
                val (slotId, slotText) = slots[index]
                
                SlotButton(
                    text = slotText,
                    isSelected = selectedSlot == slotId,
                    onClick = { onSlotSelected(slotId) }
                )
            }
        }
    }
}

/**
 * Кнопка-чип для выбора временного слота с цветовой индикацией
 */
@Composable
fun SlotButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val slotColors = listOf(
        Color(0xFFE3F2FD), // 1 - голубой
        Color(0xFFFCE4EC), // 2 - розовый
        Color(0xFFFFF8E1), // 3 - желтый
        Color(0xFFF1F8E9), // 4 - светло-зеленый
        Color(0xFFE0F2F1), // 5 - бирюзовый
        Color(0xFFE3F2FD), // 6 - голубой
        Color(0xFFF3E5F5), // 7 - фиолетовый
        Color(0xFFFFFDE7), // 8 - светло-желтый
        Color(0xFFFFE0B2), // 9 - оранжевый
        Color(0xFFFFCCBC), // 10 - коралловый
        Color(0xFFE1F5FE), // 11 - голубой
        Color(0xFFFFF8E1)   // 12 - желтый
    )
    
    val color = slotColors.getOrElse((text.split(" ").lastOrNull()?.toIntOrNull() ?: 1) - 1) { 
        Color.White 
    }
    
    Chip(
        onClick = onClick,
        selected = isSelected,
        colors = ChipDefaults.chipColors(
            containerColor = if (isSelected) color else Color.LightGray.copy(alpha = 0.3f),
            contentColor = if (isSelected) Color.Black else Color.White,
            disabledContainerColor = Color.LightGray.copy(alpha = 0.5f)
        ),
        modifier = Modifier.height(48.dp).widthIn(min = 120.dp)
    ) {
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}

/**
 * Экран просмотра и редактирования существующей заметки (для навигации)
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