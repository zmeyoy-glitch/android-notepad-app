package com.example.notepad.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.notepad.data.NoteEntity

/**
 * Базовый каркас MainActivity для просмотра заметки
 */
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Просмотр заметки",
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Отображение текущей заметки (с временным слотом и статусом)
                        val currentNote by remember { mutableStateOf<NoteEntity?>(null) }
                        
                        if (currentNote != null) {
                            NotePreviewCard(note = currentNote!!)
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Кнопка для обновления заметки из базы данных
                            Button(
                                onClick = {
                                    // Загрузка заметки из БД (будет реализована позже)
                                    loadNoteFromDatabase()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Обновить из БД")
                            }
                        } else {
                            Text(
                                text = "Заметка не загружена",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Кнопка для загрузки первой заметки из базы данных
                            Button(
                                onClick = {
                                    // Загрузка первой заметки (будет реализована позже)
                                    loadFirstNoteFromDatabase()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Загрузить первую заметку")
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Загрузка конкретной заметки по ID из базы данных (будет реализована позже)
     */
    private fun loadNoteFromDatabase() {
        // Реализация будет добавлена в NoteRepository
        currentNote = null
    }
    
    /**
     * Загрузка первой заметки из базы данных (будет реализована позже)
     */
    private fun loadFirstNoteFromDatabase() {
        // Реализация будет добавлена в NoteRepository
        currentNote = null
    }
}

/**
 * Карточка предпросмотра заметки (с временным слотом и статусом)
 */
@Composable
fun NotePreviewCard(note: NoteEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Заголовок заметки (с временным слотом и статусом)
            Text(
                text = note.title.ifEmpty { "Без заголовка" },
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Контент заметки (с временным слотом и статусом)
            Text(
                text = note.content.ifEmpty { "Без контента" },
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 10
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Временной слот (с временным слотом и статусом)
            Text(
                text = "Время: ${getSlotTime(note.startTime)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Статус заметки (с временным слотом и статусом)
            val statusText = when {
                note.status == 0 -> "Черновик"
                note.status == 1 -> "Опубликовано"
                else -> "Неизвестный статус"
            }
            
            Text(
                text = "Статус: $statusText",
                style = MaterialTheme.typography.bodyMedium,
                color = if (note.status == 0) 
                    MaterialTheme.colorScheme.error 
                else 
                    MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * Преобразование временного слота в читаемое время (с временным слотом и статусом)
 */
fun getSlotTime(slot: Long): String {
    return when (slot) {
        1L -> "17:00"
        2L -> "17:15"
        3L -> "17:30"
        4L -> "17:45"
        5L -> "18:00"
        6L -> "18:15"
        7L -> "18:30"
        8L -> "18:45"
        9L -> "19:00"
        10L -> "19:15"
        11L -> "19:30"
        12L -> "19:45"
        else -> "Неизвестное время"
    }
}

/**
 * Расширение для удобного преобразования временного слота в читаемое время (с временным слотом и статусом)
 */
fun NoteEntity.getSlotTime(): String {
    return getSlotTime(this.startTime)
}