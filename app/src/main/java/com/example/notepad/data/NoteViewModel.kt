package com.example.notepad.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel для управления списком заметок с временными слотами и статусами
 */
class NoteViewModel : ViewModel() {
    
    // Repository для работы с базой данных
    private val repository = NoteRepository()
    
    /**
     * Flow для получения всех заметок из базы данных (с временными слотами и статусами)
     */
    val notesFlow: StateFlow<List<NoteEntity>> = repository.getAllNotes().stateIn(
        scope = viewModelScope,
        initialValue = emptyList(),
        key = "notes"
    )
    
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
     * Удаление заметки из базы данных (с временным слотом и статусом)
     */
    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            repository.deleteNote(
                note = NoteEntity(id = noteId, title = "", content = "", startTime = 1, status = 0)
            )
        }
    }
}

/**
 * Расширение для удобного обновления заметки с временным слотом и статусом
 */
fun NoteViewModel.updateNote(
    note: NoteEntity
): Unit {
    this.updateNote(
        noteId = note.id,
        title = note.title.ifEmpty { "Без названия" },
        content = note.content,
        startTime = note.startTime,
        status = note.status
    )
}

/**
 * Расширение для удобного сохранения новой заметки с временным слотом и статусом
 */
fun NoteViewModel.saveNote(
    title: String,
    content: String,
    startTime: Long, // 1-12 (временной слот)
    status: Int      // 0 - черновик, 1 - опубликовано
): Unit {
    this.saveNote(
        title = title.ifEmpty { "Без названия" },
        content = content,
        startTime = startTime,
        status = status
    )
}