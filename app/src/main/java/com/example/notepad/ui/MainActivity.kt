package com.example.notepad.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Главный экран приложения с навигацией между списком заметок и редактором
 */
@Composable
fun MainActivity() {
    // Состояние навигации: 0 - список заметок, 1 - редактор, 2 - просмотр заметки
    var currentScreen by remember { mutableStateOf(0) }
    
    // ID редактируемой или просматриваемой заметки (null для списка/редактора новых заметок)
    var noteId by remember { mutableStateOf<Long?>(null) }
    
    // Текущая заметка в редакторе или просмотре
    var currentNote by remember { mutableStateOf<NoteEntity?>(null) }
    
    // ViewModel для управления списком заметок
    val notesViewModel = NoteViewModel()
    
    // ViewModel для управления редактором заметки
    val editorViewModel = NoteEditorViewModel()
    
    // ViewModel для просмотра конкретной заметки
    val viewViewModel = NoteViewViewModel()
    
    when (currentScreen) {
        0 -> NoteListScreen(
            notesFlow = notesViewModel.notesFlow,
            onNoteClick = { noteId ->
                currentNote = noteId?.let { id -> 
                    // Загрузка заметки для просмотра или редактирования
                    viewViewModel.loadNote(id)
                    id
                } ?: null
                
                // Переход в редактор с существующей заметкой
                editorViewModel.initializeEditor(noteId)
                
                currentScreen = 1
            },
            onNewNoteClick = { 
                // Создание новой заметки с начальными значениями
                editorViewModel.initializeEditor(null)
                currentScreen = 1
            }
        )
        
        1 -> NoteEditorScreen(
            note = currentNote,
            onBackClick = { currentScreen = 0 },
            onSaveClick = { savedNote ->
                // Сохранение заметки и возврат к списку
                notesViewModel.saveNote(
                    title = savedNote.title,
                    content = savedNote.content,
                    startTime = savedNote.startTime,
                    status = savedNote.status
                )
                
                currentScreen = 0
            },
            onUpdateStartTime = { slot ->
                editorViewModel.updateStartTime(slot)
            },
            onUpdateStatus = { status ->
                editorViewModel.updateStatus(status)
            }
        )
        
        2 -> NoteViewScreen(
            note = viewViewModel.currentNote,
            onBackClick = { currentScreen = 0 },
            onEditClick = { 
                // Переход в редактор с существующей заметкой
                editorViewModel.initializeEditor(noteId)
                currentScreen = 1
            }
        )
    }
}

/**
 * Экран списка заметок с отображением временных слотов и статусов
 */
@Composable
fun NoteListScreen(
    notesFlow: androidx.lifecycle.StateFlow<List<NoteEntity>>,
    onNoteClick: (Long?) -> Unit,
    onNewNoteClick: () -> Unit
) {
    // Получение всех заметок из ViewModel
    val notes by notesFlow.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📝 Мои заметки") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // Кнопка добавления новой заметки
            FloatingActionButton(
                onClick = onNewNoteClick,
                modifier = Modifier.align(Alignment.End),
                colors = FloatingActionButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Новая заметка")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Список заметок с временными слотами и статусами
            if (notes.isEmpty()) {
                EmptyState("Пока нет заметок", "Нажмите +, чтобы создать первую заметку")
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(notes.size) { index ->
                        val note = notes[index]
                        
                        // Карточка заметки с временным слотом и статусом
                        NoteCard(
                            note = note,
                            onClick = { onNoteClick(note.id) }
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

/**
 * Экран просмотра конкретной заметки с временным слотом и статусом
 */
@Composable
fun NoteViewScreen(
    note: NoteEntity?,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    if (note == null) {
        EmptyState("Заметка не найдена", "Попробуйте выбрать другую заметку")
        return
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(note.title.ifEmpty { "Без названия" }) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            ) {
                // Кнопка редактирования
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Редактировать")
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Кнопка назад
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Отображение временного слота и статуса заметки
            NoteCard(
                note = note,
                onClick = { /* Клик по карточке для просмотра */ }
            )
        }
    }
}

/**
 * Компонент пустого состояния (когда нет заметок)
 */
@Composable
fun EmptyState(title: String, subtitle: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.LightGray
        )
    }
}

/**
 * Компонент карточки заметки с отображением временного слота и статуса
 */
@Composable
fun NoteCard(
    note: NoteEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Заголовок заметки
            Text(
                text = note.title.ifEmpty { "Без названия" },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Контент заметки (ограниченный по длине)
            Text(
                text = note.content.ifEmpty { "Без контента" },
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Отображение временного слота и статуса заметки
            Row {
                TimeSlotDisplay(note)
                
                Spacer(modifier = Modifier.width(8.dp))
                
                StatusDisplay(note)
            }
        }
    }
}

/**
 * Компонент отображения временного слота в карточке заметки
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