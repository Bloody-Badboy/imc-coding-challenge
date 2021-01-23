package dev.arpan.imc.demo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import dev.arpan.imc.demo.prefs.PreferenceStorage
import dev.arpan.imc.demo.utils.Event

class MainViewModel(preferenceStorage: PreferenceStorage) : ViewModel() {
    private val _logout = MediatorLiveData<Event<Unit>>()
    val logout: LiveData<Event<Unit>>
        get() = _logout

    init {
        _logout.addSource(preferenceStorage.loggedInUserEmailObservable) { email ->
            if (email == null) {
                _logout.value = Event(Unit)
            }
        }
    }
}
