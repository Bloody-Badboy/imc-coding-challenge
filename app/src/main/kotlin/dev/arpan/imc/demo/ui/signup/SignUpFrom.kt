package dev.arpan.imc.demo.ui.signup

import dev.arpan.imc.demo.utils.LiveFrom
import dev.arpan.imc.demo.utils.Validator

class SignUpFrom : LiveFrom() {

    val firstName = Field<String>(
        validator = {
            !it.isNullOrEmpty()
        }
    )
    val lastName = Field<String>(
        validator = {
            !it.isNullOrEmpty()
        }
    )

    val mobileNumber = Field<String>(
        validator = {
            !it.isNullOrEmpty()
        }
    )

    val email = Field<String>(validator = { Validator.isValidEmail(it) })

    val password = Field<String>(validator = { Validator.isValidPassword(it) })

    val location = Field<String>(
        validator = {
            !it.isNullOrEmpty()
        }
    )

    override val fields: List<Field<*>>
        get() = listOf(firstName, lastName, email, mobileNumber, password, location)

    init {
        init()
    }
}
