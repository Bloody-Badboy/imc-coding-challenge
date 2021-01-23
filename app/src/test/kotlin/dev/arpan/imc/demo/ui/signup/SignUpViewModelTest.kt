package dev.arpan.imc.demo.ui.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.arpan.imc.demo.MOCK_USER
import dev.arpan.imc.demo.MainCoroutineRule
import dev.arpan.imc.demo.data.UserRepository
import dev.arpan.imc.demo.data.model.CurrentLocation
import dev.arpan.imc.demo.data.model.ResultWrapper
import dev.arpan.imc.demo.getOrAwaitValue
import dev.arpan.imc.demo.ui.signup.SignUpViewModel.SignUpResult
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

class SignUpViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val userRepository: UserRepository = mock()

    private fun getViewModel() = SignUpViewModel(userRepository).apply {
        from.firstName.value.value = MOCK_USER.firstName
        from.lastName.value.value = MOCK_USER.lastName
        from.mobileNumber.value.value = MOCK_USER.mobileNumber
        from.email.value.value = MOCK_USER.email
        from.password.value.value = MOCK_USER.password
    }

    @Test
    fun `signUp() error if current location not set`() {
        val viewModel = getViewModel()
        viewModel.signUp()
        val result = viewModel.signUpResult.getOrAwaitValue()
        assertThat(result.peekContent()).isInstanceOf(SignUpResult.Error::class.java)
    }

    @Test
    fun `signUp() error`() = runBlockingTest {
        whenever(userRepository.register(any())).thenReturn(ResultWrapper.Error())
        val viewModel = getViewModel()
        viewModel.setCurrentLocation(CurrentLocation(MOCK_USER.latitude, MOCK_USER.longitude))
        viewModel.signUp()
        val result = viewModel.signUpResult.getOrAwaitValue()
        assertThat(result.peekContent()).isInstanceOf(SignUpResult.Error::class.java)
        verify(userRepository).register(MOCK_USER.copy(id = 0))
    }

    @Test
    fun `signUp() success`() = runBlockingTest {
        whenever(userRepository.register(any())).thenReturn(ResultWrapper.Success(Unit))
        val viewModel = getViewModel()
        viewModel.setCurrentLocation(CurrentLocation(MOCK_USER.latitude, MOCK_USER.longitude))
        viewModel.signUp()
        val result = viewModel.signUpResult.getOrAwaitValue()
        assertThat(result.peekContent()).isInstanceOf(SignUpResult.Success::class.java)
        verify(userRepository).register(MOCK_USER.copy(id = 0))
    }
}
