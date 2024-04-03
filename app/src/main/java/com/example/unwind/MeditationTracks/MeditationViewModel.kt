package com.example.unwind.MeditationTracks
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.unwind.MeditationTracks.MeditationDatabase
import com.example.unwind.MeditationTracks.MeditationRepository
import com.example.unwind.MeditationTracks.MeditationTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MeditationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MeditationRepository
    val allTracks: LiveData<List<MeditationTrack>>

    init {
        val dao = MeditationDatabase.getDatabase(application).meditationTrackDao()
        repository = MeditationRepository(dao)
        allTracks = repository.allTracks
    }

    fun insert(track: MeditationTrack) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(track)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }
}
