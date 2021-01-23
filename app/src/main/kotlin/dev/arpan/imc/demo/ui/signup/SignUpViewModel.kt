package dev.arpan.imc.demo.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.arpan.imc.demo.data.UserRepository
import dev.arpan.imc.demo.data.model.CurrentLocation
import dev.arpan.imc.demo.data.model.ResultWrapper
import dev.arpan.imc.demo.data.model.User
import dev.arpan.imc.demo.utils.Event
import kotlinx.coroutines.launch

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _isOngoing = MutableLiveData<Boolean>()
    val isOngoing: LiveData<Boolean>
        get() = _isOngoing

    private val _isRequestingLocationUpdate = MutableLiveData<Boolean>()
    val isRequestingLocationUpdate: LiveData<Boolean>
        get() = _isRequestingLocationUpdate

    private val _signUpResult = MutableLiveData<Event<SignUpResult>>()
    val signUpResult: LiveData<Event<SignUpResult>>
        get() = _signUpResult

    val from = SignUpFrom()

    private var currentLocation: CurrentLocation? = null

    fun signUp() {
        val location = currentLocation
        if (location == null) {
            _signUpResult.value = Event(SignUpResult.Error())
            return
        }
        val user = User(
            firstName = from.firstName.value.value.orEmpty(),
            lastName = from.lastName.value.value.orEmpty(),
            mobileNumber = from.mobileNumber.value.value.orEmpty(),
            email = from.email.value.value.orEmpty(),
            password = from.password.value.value.orEmpty(),
            latitude = location.latitude,
            longitude = location.longitude
        )

        viewModelScope.launch {
            _isOngoing.value = true
            val result = userRepository.register(user)
            _isOngoing.value = false

            when (result) {
                is ResultWrapper.Success -> {
                    _signUpResult.value = Event(SignUpResult.Success)
                }
                is ResultWrapper.Error -> {
                    _signUpResult.value = Event(SignUpResult.Error(result.throwable))
                }
            }
        }
    }

    fun setRequestingLocationUpdate(requesting: Boolean) {
        _isRequestingLocationUpdate.value = requesting
    }

    fun setCurrentLocation(location: CurrentLocation) {
        currentLocation = location
        from.location.value.value = "${location.latitude}, ${location.longitude}"
    }

    sealed class SignUpResult {
        object Success : SignUpResult()
        data class Error(val throwable: Throwable? = null) : SignUpResult()
    }
}
