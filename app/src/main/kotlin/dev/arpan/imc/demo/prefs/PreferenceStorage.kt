package dev.arpan.imc.demo.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface PreferenceStorage {
    var loggedInUserEmail: String?

    val loggedInUserEmailObservable: LiveData<String?>

    var notifyTriggerMillis: Long
}

fun PreferenceStorage.logout() {
    loggedInUserEmail = null
}

class SharedPreferenceStorage(
    private val context: Context,
) : PreferenceStorage {

    companion object {
        const val PREFS_NAME = "imc_demo"
        const val PREF_DARK_MODE_ENABLED = "pref_logged_in_user_email"
        const val PREF_NOTIFY_TRIGGER_MILLIS = "pref_notify_trigger_millis"
    }

    private val prefs: Lazy<SharedPreferences> = lazy {
        context.applicationContext.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        ).apply {
            registerOnSharedPreferenceChangeListener(changeListener)
        }
    }

    private val _observableSelectedThemeResult = MutableLiveData<String>()

    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            PREF_DARK_MODE_ENABLED -> _observableSelectedThemeResult.value = loggedInUserEmail
        }
    }

    override var loggedInUserEmail: String? by StringPreference(
        prefs,
        PREF_DARK_MODE_ENABLED,
        null
    )

    override var notifyTriggerMillis: Long by LongPreference(
        prefs,
        PREF_NOTIFY_TRIGGER_MILLIS,
        0
    )
    override val loggedInUserEmailObservable: LiveData<String?>
        get() = _observableSelectedThemeResult
}

class StringPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: String?
) : ReadWriteProperty<Any, String?> {

    override fun getValue(
        thisRef: Any,
        property: KProperty<*>
    ): String? {
        return preferences.value.getString(name, defaultValue)
    }

    override fun setValue(
        thisRef: Any,
        property: KProperty<*>,
        value: String?
    ) {
        preferences.value.edit { putString(name, value) }
    }
}

class LongPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Long
) : ReadWriteProperty<Any, Long> {

    override fun getValue(
        thisRef: Any,
        property: KProperty<*>
    ): Long {
        return preferences.value.getLong(name, defaultValue)
    }

    override fun setValue(
        thisRef: Any,
        property: KProperty<*>,
        value: Long
    ) {
        preferences.value.edit { putLong(name, value) }
    }
}
