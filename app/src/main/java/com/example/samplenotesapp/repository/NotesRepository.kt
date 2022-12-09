package com.example.samplenotesapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.samplenotesapp.api.NotesApi
import com.example.samplenotesapp.models.NoteRequest
import com.example.samplenotesapp.models.NoteResponse
import com.example.samplenotesapp.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NotesRepository @Inject constructor(val notesApi: NotesApi) {

    private var _notesResponse = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val notesResponse :LiveData<NetworkResult<List<NoteResponse>>>
    get() = _notesResponse

    private var _statusLiveData =MutableLiveData<NetworkResult<String>>()
    val statusLiveData :LiveData<NetworkResult<String>>
    get() = _statusLiveData
    suspend fun getNotes(){
        val response =notesApi.getNotes()
        _notesResponse.postValue(NetworkResult.Loading())
        if(response.isSuccessful && response.body() != null){
            _notesResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null){
            val errorObj =JSONObject(response.errorBody()!!.charStream().readText())
            _notesResponse.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _notesResponse.postValue(NetworkResult.Error("something went wrong"))
        }
    }

    suspend fun createNote(noteRequest: NoteRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response  =notesApi.createNote(noteRequest)
        handleResponse(response,"created Note")
    }

    private fun handleResponse(response: Response<NoteResponse> , message:String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(message))
        } else {
            _statusLiveData.postValue(NetworkResult.Error("something went wrong"))
        }
    }

    suspend fun deleteNote(noteId :String){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response  =notesApi.deleteNote(noteId)
        handleResponse(response,"Deleted Note")
    }

    suspend fun updateNote(noteId:String,noteRequest: NoteRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response  =notesApi.updateNote(noteId, noteRequest)
        handleResponse(response,"updated Note")
    }

}