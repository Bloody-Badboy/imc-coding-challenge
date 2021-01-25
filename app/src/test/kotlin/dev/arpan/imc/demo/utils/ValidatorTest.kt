package dev.arpan.imc.demo.utils

import com.google.common.truth.Truth.assertThat
import dev.arpan.imc.demo.utils.Validator.isValidEmail
import dev.arpan.imc.demo.utils.Validator.isValidPassword
import org.junit.Test

class ValidatorTest {

    @Test
    fun `validate email`() {
        assertThat(isValidEmail("example@example.com")).isTrue()
        assertThat(isValidEmail("1234567890@example.com")).isTrue()
        assertThat(isValidEmail("email@example.co.jp")).isTrue()

        assertThat(isValidEmail("@example.com")).isFalse()
        assertThat(isValidEmail("email@example@example.com")).isFalse()
    }

    @Test
    fun `password contains at least one number, one uppercase, one lowercase`() {
        assertThat(isValidPassword("Abc123")).isTrue()
        assertThat(isValidPassword("Abc@123")).isTrue()

        assertThat(isValidPassword("abc")).isFalse()
        assertThat(isValidPassword("ABC123")).isFalse()
        assertThat(isValidPassword("12345678")).isFalse()
        assertThat(isValidPassword("abc123")).isFalse()
    }
}
