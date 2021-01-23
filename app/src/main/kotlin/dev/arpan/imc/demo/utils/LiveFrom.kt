package dev.arpan.imc.demo.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

abstract class LiveFrom {
    private val _isValid = MediatorLiveData<Boolean>()
    val isValid: LiveData<Boolean>
        get() = _isValid

    abstract val fields: List<Field<*>>

    fun init() {
        fields.forEach { inputField ->
            _isValid.addSource(inputField.value) { changedValue ->
                inputField.validate(changedValue)
                _isValid.value = fields.all {
                    it.isValid
                }
            }
        }
    }

    inner class Field<T>(private val validator: ((T?) -> Boolean) = { true }) {

        private val _isError = MutableLiveData<Boolean>()
        val isError: LiveData<Boolean>
            get() = _isError

        val value = MutableLiveData<T>()

        var isValid = false

        @Suppress("UNCHECKED_CAST")
        fun validate(value: Any?) {
            isValid = validator(value as T?)
            _isError.value = !isValid
        }
    }
}
