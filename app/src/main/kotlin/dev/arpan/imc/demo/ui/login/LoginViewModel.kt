package dev.arpan.imc.demo.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.arpan.imc.demo.data.UserRepository
import dev.arpan.imc.demo.prefs.PreferenceStorage
import dev.arpan.imc.demo.utils.Event
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
    private val preferenceStorage: PreferenceStorage
) : ViewModel() {
    private val _isOngoing = MutableLiveData<Boolean>()
    val isOngoing: LiveData<Boolean>
        get() = _isOngoing

    private val _loginResult = MutableLiveData<Event<LoginResult>>()
    val loginResult: LiveData<Event<LoginResult>>
        get() = _loginResult

    private val _navigateToSignUp = MutableLiveData<Event<Unit>>()
    val navigateToSignUp: LiveData<Event<Unit>>
        get() = _navigateToSignUp

    val from = LoginFrom()

    init {
        if (preferenceStorage.loggedInUserEmail != null) {
            _loginResult.value = Event(LoginResult.Success)
        }
    }

    fun toSignUp() {
        _navigateToSignUp.value = Event(Unit)
    }

    fun login() {
        val email = from.email.value.value.orEmpty()
        val password = from.password.value.value.orEmpty()

        viewModelScope.launch {
            _isOngoing.value = true
            val user = userRepository.login(email, password)
            _isOngoing.value = false

            if (user != null) {
                preferenceStorage.loggedInUserEmail = user.email
                _loginResult.value = Event(LoginResult.Success)
            } else {
                _loginResult.value = Event(LoginResult.Error)
            }
        }
    }

    sealed class LoginResult {
        object Success : LoginResult()
        object Error : LoginResult()
    }
}
