package dev.arpan.imc.demo.ui.login

import dev.arpan.imc.demo.utils.LiveFrom
import dev.arpan.imc.demo.utils.Validator

class LoginFrom : LiveFrom() {

    val email = Field<String>(validator = { Validator.isValidEmail(it) })
    val password = Field<String>(validator = { Validator.isValidPassword(it) })

    override val fields: List<Field<*>>
        get() = listOf(email, password)

    init {
        init()
    }
}
