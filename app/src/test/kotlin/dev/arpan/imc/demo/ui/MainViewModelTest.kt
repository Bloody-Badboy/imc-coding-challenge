package dev.arpan.imc.demo.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dev.arpan.imc.demo.MOCK_USER
import dev.arpan.imc.demo.MainCoroutineRule
import dev.arpan.imc.demo.getOrAwaitValue
import dev.arpan.imc.demo.prefs.PreferenceStorage
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val preferenceStorage: PreferenceStorage = mock()

    @Test
    fun `logout event if logged in email observable is null`() {
        val observableEmail = MutableLiveData<String>()
        whenever(preferenceStorage.loggedInUserEmailObservable).thenReturn(observableEmail)
        val viewModel = MainViewModel(preferenceStorage)
        observableEmail.value = null
        viewModel.logout.getOrAwaitValue()
    }

    @Test(expected = TimeoutException::class)
    fun `no logout event if logged in email observable in not null`() {
        val observableEmail = MutableLiveData<String>()
        whenever(preferenceStorage.loggedInUserEmailObservable).thenReturn(observableEmail)
        val viewModel = MainViewModel(preferenceStorage)
        observableEmail.value = MOCK_USER.email
        viewModel.logout.getOrAwaitValue()
    }
}