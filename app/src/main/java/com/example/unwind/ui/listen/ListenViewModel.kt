package com.example.unwind.ui.listen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListenViewModel : ViewModel() {

    private val _spotifyToken = MutableLiveData<String>()
    val spotifyToken: LiveData<String> = _spotifyToken

    fun updateSpotifyToken(newToken: String) {
        _spotifyToken.postValue(newToken)
        // Here you can also trigger fetching Spotify data with the new token
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is listen Fragment"
    }
    val text: LiveData<String> = _text
}