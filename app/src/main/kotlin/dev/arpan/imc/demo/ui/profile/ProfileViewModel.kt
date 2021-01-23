package dev.arpan.imc.demo.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.arpan.imc.demo.data.UserRepository
import dev.arpan.imc.demo.data.model.User
import dev.arpan.imc.demo.prefs.PreferenceStorage
import dev.arpan.imc.demo.prefs.logout
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val preferenceStorage: PreferenceStorage,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    init {
        val email = preferenceStorage.loggedInUserEmail
        if (email != null) {
            viewModelScope.launch {
                val user = userRepository.getUserDetails(email)
                if (user != null) {
                    _user.value = user
                } else {
                    logout()
                }
            }
        } else {
            logout()
        }
    }

    fun logout() {
        preferenceStorage.logout()
    }
}
