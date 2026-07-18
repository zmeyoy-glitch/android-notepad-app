package com.example.notepad.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.notepad.data.NoteDatabase
import com.example.notepad.data.NoteEntity

/**
 * Главный экран приложения - список заметок
 */
class MainActivity : ComponentActivity() {
    
    companion object {
        private const val DB_NAME = "notepad_database"
    }
    
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Инициализация базы данных Room
        val database = androidx.room.Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            DB_NAME
        ).build()
        
        setContent {
            NotepadTheme {
                MaterialTheme {
                    MainScreen(database)
                }
            }
        }
    }
}

/**
 * UI главного экрана с списком заметок
 */
@Composable
fun MainScreen(database: androidx.room.RoomDatabase) {
    // Получаем DAO из базы данных
    val noteDao = object : androidx.room.RoomDatabase.NoteDao() {
        override suspend fun insert(note: com.example.notepad.data.Note): Long = 
            super.insert(note)
        
        override suspend fun update(note: com.example.notepad.data.Note) = 
            super.update(note)
        
        override suspend fun getAll(): List<com.example.notepad.data.Note> = 
            super.getAll()
        
        override suspend fun getById(id: Long): com.example.notepad.data.Note? = 
            super.getById(id)
        
        override suspend fun delete(id: Long) = 
            super.delete(id)
    }
    
    // Состояние заметок
    var notes by remember { mutableStateOf(emptyList<com.example.notepad.data.Note>()) }
    
    // Загрузка заметок при старте
    LaunchedEffect(Unit) {
        val allNotes = noteDao.getAll()
        notes = allNotes.sortedByDescending { it.createdAt }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои заметки", fontWeight = FontWeight.Bold) },
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
            // Кнопка добавления заметки
            FloatingActionButton(
                onClick = { /* Переход к созданию новой заметки */ },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить заметку")
            }
            
            // Список заметок или пустое состояние
            if (notes.isEmpty()) {
                EmptyState()
            } else {
                NotesList(notes, noteDao)
            }
        }
    }
}

/**
 * Компонент для отображения пустого состояния
 */
@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Заметок пока нет", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Нажмите +, чтобы добавить первую заметку", 
             style = MaterialTheme.typography.bodySmall,
             color = Color.Gray)
    }
}

/**
 * Список заметок с возможностью прокрутки
 */
@Composable
fun NotesList(notes: List<com.example.notepad.data.Note>, noteDao: androidx.room.RoomDatabase.NoteDao) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(notes.size) { index ->
            val note = notes[index]
            NoteCard(note, noteDao)
        }
    }
}

/**
 * Карточка заметки с информацией о временном слоте и статусе
 */
@Composable
fun NoteCard(
    note: com.example.notepad.data.Note,
    noteDao: androidx.room.RoomDatabase.NoteDao
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (note.status) {
                0 -> Color(0xFFE3F2FD) // Draft - голубой
                1 -> Color(0xFFE8F5E9) // Published - зеленый
                else -> Color(0xFFFFF3E0) // Archived - оранжевый
            }
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Заголовок заметки
            Text(
                text = note.title ?: "Без названия",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Временной слот
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
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Контент заметки (ограниченный)
            val contentPreview = note.content?.take(100) ?: "Пусто"
            Text(
                text = contentPreview,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Статус заметки
            val statusText = when (note.status) {
                0 -> "Черновик"
                1 -> "Опубликовано"
                else -> "Архив"
            }
            Text(
                text = statusText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

/**
 * Тема приложения с акцентным цветом
 */
@Composable
fun NotepadTheme(content: @Composable () -> Unit) {
    val accentColor = Color(0xFF6200EE) // Ваш акцентный цвет
    
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = accentColor,
            onPrimary = Color.White,
            secondary = Color(0xFF03DAC5),
            background = Color(0xFFF5F5F5),
            surface = Color.White,
            error = Color.Red
        ),
        content = content
    )
}

/**
 * Функция для создания darkColorScheme из light
 */
fun darkColorScheme(
    primary: Color,
    onPrimary: Color,
    secondary: Color,
    background: Color,
    surface: Color,
    error: Color
): androidx.compose.ui.graphics.ColorScheme {
    return androidx.compose.material3.lightColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        secondary = secondary,
        background = background,
        surface = surface,
        error = error
    )
}