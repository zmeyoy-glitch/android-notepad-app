package com.example.notepad.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel для просмотра конкретной заметки с временными слотами и статусами
 */
class NoteViewViewModel : ViewModel() {
    
    // Repository для работы с базой данных
    private val repository = NoteRepository()
    
    /**
     * Текущая заметка в просмотре (с временным слотом и статусом)
     */
    var currentNote by mutableStateOf<NoteEntity?>(null)
        private set
    
    /**
     * Flow для получения текущей заметки из базы данных (с временными слотами и статусами)
     */
    val noteFlow: StateFlow<NoteEntity?> = repository.getNoteById(0L).stateIn(
        scope = viewModelScope,
        initialValue = null,
        key = "viewNote"
    )
    
    /**
     * Загрузка конкретной заметки из базы данных (с временными слотами и статусами)
     */
    fun loadNote(noteId: Long) {
        viewModelScope.launch {
            repository.getNoteById(noteId).collectAsState(initial = null).value?.let { note ->
                currentNote = note
            } ?: run {
                // Если заметка не найдена, создаем заглушку
                currentNote = NoteEntity(0L, "Заметка не найдена", "", 1, 0)
            }
        }
    }
    
    /**
     * Обновление текущей заметки в просмотре (с временными слотами и статусами)
     */
    fun updateNote(note: NoteEntity) {
        currentNote = note
    }
}

/**
 * Расширение для удобного обновления заметки в просмотре (с временными слотами и статусами)
 */
fun NoteViewViewModel.updateNote(note: NoteEntity) {
    this.updateNote(note)
}