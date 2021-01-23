package dev.arpan.imc.demo.utils

import java.util.regex.Pattern

object Validator {
    private val EMAIL_ADDRESS = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
    )
    private val PASSWORD = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).*\$")

    fun isValidEmail(email: String?) =
        (!email.isNullOrEmpty() && EMAIL_ADDRESS.matcher(email).matches())

    fun isValidPassword(password: String?) =
        (!password.isNullOrEmpty() && PASSWORD.matcher(password).matches())
}
