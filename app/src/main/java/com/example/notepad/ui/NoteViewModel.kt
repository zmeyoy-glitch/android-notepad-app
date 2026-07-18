package com.example.notepad.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepad.data.NoteEntity
import com.example.notepad.data.NoteRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel для управления списком заметок с временными слотами и статусами
 */
class NoteViewModel(
    private val repository: NoteRepository = NoteRepository()
) : ViewModel() {
    
    // Список всех заметок (с временными слотами и статусами)
    val notesFlow: StateFlow<List<NoteEntity>> = repository.notesFlow
    
    /**
     * Сохранение новой или существующей заметки с временным слотом и статусом
     */
    fun saveNote(
        title: String,
        content: String,
        startTime: Long, // 1-12 (временной слот)
        status: Int      // 0 - черновик, 1 - опубликовано
    ) {
        viewModelScope.launch {
            repository.saveNote(
                title = title.ifEmpty { "Без названия" },
                content = content,
                startTime = startTime,
                status = status
            )
        }
    }
    
    /**
     * Обновление существующей заметки с временным слотом и статусом
     */
    fun updateNote(
        noteId: Long,
        title: String,
        content: String,
        startTime: Long, // 1-12 (временной слот)
        status: Int      // 0 - черновик, 1 - опубликовано
    ) {
        viewModelScope.launch {
            repository.updateNote(
                noteId = noteId,
                title = title.ifEmpty { "Без названия" },
                content = content,
                startTime = startTime,
                status = status
            )
        }
    }
    
    /**
     * Удаление заметки с временным слотом и статусом
     */
    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            repository.deleteNote(noteId)
        }
    }
    
    /**
     * Получение конкретной заметки по ID (с временными слотами и статусами)
     */
    fun getNoteById(noteId: Long): Flow<NoteEntity?> = repository.getNoteById(noteId)
}

/**
 * ViewModel для управления редактором заметки с выбором времени и статуса
 */
class NoteEditorViewModel(
    private val repository: NoteRepository = NoteRepository()
) : ViewModel() {
    
    // Существующая заметка (если редактируем существующую) или null (новая заметка)
    var existingNote: NoteEntity? by mutableStateOf(null)
        private set
    
    /**
     * Инициализация редактора с существующей заметкой или создание новой
     */
    fun initializeEditor(noteId: Long?) {
        if (noteId != null) {
            viewModelScope.launch {
                existingNote = repository.getNoteById(noteId).firstOrNull()
            }
        } else {
            // Новая заметка с начальными значениями
            existingNote = NoteEntity(0L, "Без названия", "", 1, 0) // startTime=1 (17:00-17:15), status=0 (черновик)
        }
    }
    
    /**
     * Сохранение заметки с временным слотом и статусом
     */
    fun saveNote(
        title: String,
        content: String,
        startTime: Long, // 1-12 (временной слот)
        status: Int      // 0 - черновик, 1 - опубликовано
    ) {
        viewModelScope.launch {
            if (existingNote?.id != null && existingNote?.id > 0) {
                // Обновление существующей заметки
                repository.updateNote(
                    noteId = existingNote.id,
                    title = title.ifEmpty { "Без названия" },
                    content = content,
                    startTime = startTime,
                    status = status
                )
            } else {
                // Создание новой заметки
                repository.saveNote(
                    title = title.ifEmpty { "Без названия" },
                    content = content,
                    startTime = startTime,
                    status = status
                )
            }
        }
    }
    
    /**
     * Обновление временного слота в редакторе
     */
    fun updateStartTime(startTime: Long) {
        existingNote = existingNote?.copy(startTime = startTime) ?: NoteEntity(0L, "Без названия", "", 1, 0)
    }
    
    /**
     * Обновление статуса в редакторе
     */
    fun updateStatus(status: Int) {
        existingNote = existingNote?.copy(status = status) ?: NoteEntity(0L, "Без названия", "", 1, 0)
    }
}

/**
 * ViewModel для просмотра конкретной заметки с временным слотом и статусом
 */
class NoteViewViewModel(
    private val repository: NoteRepository = NoteRepository()
) : ViewModel() {
    
    // Конкретная заметка для просмотра (с временными слотами и статусами)
    var currentNote: NoteEntity? by mutableStateOf(null)
        private set
    
    /**
     * Загрузка конкретной заметки по ID (с временными слотами и статусами)
     */
    fun loadNote(noteId: Long) {
        viewModelScope.launch {
            currentNote = repository.getNoteById(noteId).firstOrNull()
        }
    }
    
    /**
     * Обновление заметки в редакторе (возврат к редактированию)
     */
    fun updateExistingNote(note: NoteEntity?) {
        currentNote = note
    }
}