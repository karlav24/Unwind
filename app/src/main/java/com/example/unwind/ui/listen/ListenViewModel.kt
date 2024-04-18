package com.example.unwind.ui.listen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListenViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is listen Fragment"
    }
    val text: LiveData<String> = _text
}