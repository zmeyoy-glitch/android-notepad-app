package com.example.notepad.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepad.data.NoteEntity
import com.example.notepad.data.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel для просмотра конкретной заметки (с временными слотами и статусами)
 */
class NoteViewViewModel(
    private val repository: NoteRepository
) : ViewModel() {
    
    /**
     * Текущая загруженная заметка (с временным слотом и статусом)
     */
    private val _currentNote = MutableStateFlow<NoteEntity?>(null)
    val currentNote: StateFlow<NoteEntity?> = _currentNote
    
    /**
     * Загрузка конкретной заметки по ID из базы данных (с временными слотами и статусами)
     */
    fun loadNote(noteId: Long) {
        viewModelScope.launch {
            repository.getNoteById(noteId).collect { note ->
                _currentNote.value = note
            }
        }
    }
    
    /**
     * Сохранение новой или существующей заметки в базе данных (с временным слотом и статусом)
     */
    fun saveNote(
        title: String,
        content: String,
        startTime: Long = 1L, // По умолчанию 17:00
        status: Int = 0       // По умолчанию черновик
    ) {
        viewModelScope.launch {
            repository.saveNote(title, content, startTime, status)
            _currentNote.value = NoteEntity(0L, title, content, startTime, status)
        }
    }
    
    /**
     * Обновление существующей заметки в базе данных (с временным слотом и статусом)
     */
    fun updateNote(
        noteId: Long,
        title: String,
        content: String,
        startTime: Long = 1L, // По умолчанию 17:00
        status: Int = 0       // По умолчанию черновик
    ) {
        viewModelScope.launch {
            repository.updateNote(noteId, title, content, startTime, status)
            _currentNote.value = NoteEntity(noteId, title, content, startTime, status)
        }
    }
    
    /**
     * Удаление заметки из базы данных (с временным слотом и статусом)
     */
    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.deleteNote(note)
            _currentNote.value = null
        }
    }
}

/**
 * Расширение для удобного сохранения заметки в базе данных (с временным слотом и статусом)
 */
fun NoteViewViewModel.saveNote(
    title: String,
    content: String,
    startTime: Long = 1L, // По умолчанию 17:00
    status: Int = 0       // По умолчанию черновик
): Unit {
    this.saveNote(title, content, startTime, status)
}

/**
 * Расширение для удобного обновления заметки в базе данных (с временным слотом и статусом)
 */
fun NoteViewViewModel.updateNote(
    noteId: Long,
    title: String,
    content: String,
    startTime: Long = 1L, // По умолчанию 17:00
    status: Int = 0       // По умолчанию черновик
): Unit {
    this.updateNote(noteId, title, content, startTime, status)
}

/**
 * Расширение для удобного удаления заметки из базы данных (с временным слотом и статусом)
 */
fun NoteViewViewModel.deleteNote(
    note: NoteEntity
): Unit {
    this.deleteNote(note)
}