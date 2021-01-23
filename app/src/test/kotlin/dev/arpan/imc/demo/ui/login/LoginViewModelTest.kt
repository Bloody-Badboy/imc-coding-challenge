package dev.arpan.imc.demo.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.atMost
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.arpan.imc.demo.MOCK_USER
import dev.arpan.imc.demo.MainCoroutineRule
import dev.arpan.imc.demo.data.UserRepository
import dev.arpan.imc.demo.getOrAwaitValue
import dev.arpan.imc.demo.prefs.PreferenceStorage
import dev.arpan.imc.demo.ui.login.LoginViewModel.LoginResult
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val userRepository: UserRepository = mock()
    private val preferenceStorage: PreferenceStorage = mock()

    @Test
    fun `login success if user already logged in`() {
        whenever(preferenceStorage.loggedInUserEmail).thenReturn(MOCK_USER.email)

        val viewModel = LoginViewModel(userRepository, preferenceStorage)

        val result = viewModel.loginResult.getOrAwaitValue()

        assertThat(result.peekContent()).isInstanceOf(LoginResult.Success::class.java)
    }

    @Test
    fun `toSignUp() emits navigates to sign up event`() {
        val viewModel = LoginViewModel(userRepository, preferenceStorage)
        viewModel.toSignUp()
        viewModel.navigateToSignUp.getOrAwaitValue()
    }

    @Test
    fun `login() emits success`() = runBlockingTest {
        whenever(userRepository.login(MOCK_USER.email, MOCK_USER.password)).thenReturn(MOCK_USER)

        val viewModel = LoginViewModel(userRepository, preferenceStorage)
        viewModel.from.email.value.value = MOCK_USER.email
        viewModel.from.password.value.value = MOCK_USER.password
        viewModel.login()

        val result = viewModel.loginResult.getOrAwaitValue()

        assertThat(result.peekContent()).isInstanceOf(LoginResult.Success::class.java)

        // one invocation from ViewModel <init> and another after login success
        verify(preferenceStorage, atMost(2)).loggedInUserEmail
    }

    @Test
    fun `login() emits error`() = runBlockingTest {
        whenever(userRepository.login(MOCK_USER.email, MOCK_USER.password)).thenReturn(null)

        val viewModel = LoginViewModel(userRepository, preferenceStorage)
        viewModel.from.email.value.value = MOCK_USER.email
        viewModel.from.password.value.value = MOCK_USER.password
        viewModel.login()

        val result = viewModel.loginResult.getOrAwaitValue()
        assertThat(result.peekContent()).isInstanceOf(LoginResult.Error::class.java)

        // only one invocation from ViewModel <init>
        verify(preferenceStorage, atMost(1)).loggedInUserEmail
    }
}
