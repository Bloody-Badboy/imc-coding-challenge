package dev.arpan.imc.demo.ui.home

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import dev.arpan.imc.demo.prefs.PreferenceStorage
import org.junit.Test

class HomeViewModelTest {

    private val preferenceStorage: PreferenceStorage = mock()

    @Test
    fun `setNotifyTriggerMillis saves millis in prefs`() {
        val viewModel = HomeViewModel(preferenceStorage)
        viewModel.setNotifyTriggerMillis(0L)
        verify(preferenceStorage).notifyTriggerMillis = 0
    }
}
