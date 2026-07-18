package com.example.notepad.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel для управления редактором заметки с временными слотами и статусами
 */
class NoteEditorViewModel : ViewModel() {
    
    // Repository для работы с базой данных
    private val repository = NoteRepository()
    
    /**
     * Текущая заметка в редакторе (с временным слотом и статусом)
     */
    var currentNote by mutableStateOf<NoteEntity?>(null)
        private set
    
    /**
     * Заголовок заметки в редакторе
     */
    val titleFlow: StateFlow<String> = currentNote.map { it?.title ?: "" }.stateIn(
        scope = viewModelScope,
        initialValue = "",
        key = "editorTitle"
    )
    
    /**
     * Контент заметки в редакторе
     */
    val contentFlow: StateFlow<String> = currentNote.map { it?.content ?: "" }.stateIn(
        scope = viewModelScope,
        initialValue = "",
        key = "editorContent"
    )
    
    /**
     * Временной слот заметки в редакторе (1-12)
     */
    val startTimeFlow: StateFlow<Long> = currentNote.map { it?.startTime ?: 1L }.stateIn(
        scope = viewModelScope,
        initialValue = 1L,
        key = "editorStartTime"
    )
    
    /**
     * Статус заметки в редакторе (0 - черновик, 1 - опубликовано)
     */
    val statusFlow: StateFlow<Int> = currentNote.map { it?.status ?: 0 }.stateIn(
        scope = viewModelScope,
        initialValue = 0,
        key = "editorStatus"
    )
    
    /**
     * Инициализация редактора с существующей заметкой или создание новой
     */
    fun initializeEditor(noteId: Long?) {
        if (noteId != null) {
            // Загрузка существующей заметки для редактирования
            viewModelScope.launch {
                repository.getNoteById(noteId).collectAsState(initial = null).value?.let { note ->
                    currentNote = note
                } ?: run {
                    // Если заметка не найдена, создаем новую с начальными значениями
                    currentNote = NoteEntity(0L, "Без названия", "", 1, 0)
                }
            }
        } else {
            // Создание новой заметки с начальными значениями
            currentNote = NoteEntity(0L, "Без названия", "", 1, 0)
        }
    }
    
    /**
     * Обновление временного слота в редакторе (1-12)
     */
    fun updateStartTime(slot: Long) {
        currentNote = currentNote?.copy(startTime = slot) ?: run {
            currentNote = NoteEntity(0L, "Без названия", "", slot, 0)
        }
    }
    
    /**
     * Обновление статуса в редакторе (0 - черновик, 1 - опубликовано)
     */
    fun updateStatus(status: Int) {
        currentNote = currentNote?.copy(status = status) ?: run {
            currentNote = NoteEntity(0L, "Без названия", "", 1, status)
        }
    }
    
    /**
     * Сохранение заметки в редакторе (с временным слотом и статусом)
     */
    fun saveEditorNote() {
        currentNote?.let { note ->
            viewModelScope.launch {
                repository.saveNote(
                    title = note.title,
                    content = note.content,
                    startTime = note.startTime,
                    status = note.status
                )
            }
        }
    }
    
    /**
     * Обновление существующей заметки в редакторе (с временным слотом и статусом)
     */
    fun updateExistingNote() {
        currentNote?.let { note ->
            viewModelScope.launch {
                repository.updateNote(
                    noteId = note.id,
                    title = note.title,
                    content = note.content,
                    startTime = note.startTime,
                    status = note.status
                )
            }
        }
    }
}

/**
 * Расширение для удобного обновления временного слота в редакторе
 */
fun NoteEditorViewModel.updateStartTime(slot: Long) {
    this.updateStartTime(slot)
}

/**
 * Расширение для удобного обновления статуса в редакторе
 */
fun NoteEditorViewModel.updateStatus(status: Int) {
    this.updateStatus(status)
}