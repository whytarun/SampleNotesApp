package com.example.samplenotesapp.ui.note

import androidx.lifecycle.ViewModel
import com.example.samplenotesapp.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.samplenotesapp.models.NoteRequest
import kotlinx.coroutines.launch

@HiltViewModel
class NoteViewModel @Inject constructor(private val notesRepository: NotesRepository) :ViewModel(){

    val notesLiveData get() = notesRepository.notesResponse
    val statusLiveData get() = notesRepository.statusLiveData
    fun getNotes(){
        viewModelScope.launch {
            notesRepository.getNotes()
        }
    }

    fun updateNote(noteId :String, noteRequest: NoteRequest){
        viewModelScope.launch {
            notesRepository.updateNote(noteId,noteRequest)
        }
    }

    fun deleteNote(noteId: String){
        viewModelScope.launch {
            notesRepository.deleteNote(noteId)
        }
    }

    fun createNote(noteRequest: NoteRequest){
        viewModelScope.launch {
            notesRepository.createNote(noteRequest)
        }
    }
}