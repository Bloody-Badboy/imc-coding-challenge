package dev.arpan.imc.demo.ui.home

import androidx.lifecycle.ViewModel
import dev.arpan.imc.demo.prefs.PreferenceStorage

class HomeViewModel(private val preferenceStorage: PreferenceStorage) : ViewModel() {
    fun setNotifyTriggerMillis(triggerMillis: Long) {
        preferenceStorage.notifyTriggerMillis = triggerMillis
    }
}
