package dev.arpan.imc.demo.prefs

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dev.arpan.imc.demo.MOCK_USER
import dev.arpan.imc.demo.getOrAwaitValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPreferenceStorageTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var preferenceStorage: PreferenceStorage

    @Before
    fun initPreferenceStorage() {
        preferenceStorage = SharedPreferenceStorage(ApplicationProvider.getApplicationContext())
    }

    @After
    fun cleanup() {
        preferenceStorage.loggedInUserEmail = null
    }

    @Test
    fun `observable email emit value on change`() {
        preferenceStorage.loggedInUserEmail = MOCK_USER.email
        assertThat(preferenceStorage.loggedInUserEmailObservable.getOrAwaitValue()).isEqualTo(
            MOCK_USER.email
        )
        preferenceStorage.loggedInUserEmail = null
        assertThat(preferenceStorage.loggedInUserEmailObservable.getOrAwaitValue()).isNull()
    }
}
