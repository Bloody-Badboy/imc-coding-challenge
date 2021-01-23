package dev.arpan.imc.demo.ui.profile

import androidx.lifecycle.ViewModel
import dev.arpan.imc.demo.prefs.PreferenceStorage
import dev.arpan.imc.demo.prefs.logout

class ProfileViewModel(private val preferenceStorage: PreferenceStorage) : ViewModel() {

    fun logout() {
        preferenceStorage.logout()
    }
}
