package dev.arpan.imc.demo.ui.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.arpan.imc.demo.MOCK_USER
import dev.arpan.imc.demo.MainCoroutineRule
import dev.arpan.imc.demo.data.UserRepository
import dev.arpan.imc.demo.getOrAwaitValue
import dev.arpan.imc.demo.prefs.PreferenceStorage
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val preferenceStorage: PreferenceStorage = mock()
    private val userRepository: UserRepository = mock()

    @Test
    fun `logout if logged in user email null`() {
        whenever(preferenceStorage.loggedInUserEmail).thenReturn(null)
        ProfileViewModel(preferenceStorage, userRepository)
        verify(preferenceStorage).loggedInUserEmail = null
    }

    @Test
    fun `logout if fetch user details failed`() = runBlockingTest {
        whenever(preferenceStorage.loggedInUserEmail).thenReturn(MOCK_USER.email)
        whenever(userRepository.getUserDetails(MOCK_USER.email)).thenReturn(null)
        ProfileViewModel(preferenceStorage, userRepository)
        verify(preferenceStorage).loggedInUserEmail = null
    }

    @Test
    fun `user emits on fetch user details success`() = runBlockingTest {
        whenever(preferenceStorage.loggedInUserEmail).thenReturn(MOCK_USER.email)
        whenever(userRepository.getUserDetails(MOCK_USER.email)).thenReturn(MOCK_USER)
        val viewModel = ProfileViewModel(preferenceStorage, userRepository)
        assertThat(viewModel.user.getOrAwaitValue()).isEqualTo(MOCK_USER)
    }
}