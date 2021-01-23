package dev.arpan.imc.demo.di

import android.app.Application
import android.content.Context
import dev.arpan.imc.demo.BuildConfig
import dev.arpan.imc.demo.data.DefaultUserRepository
import dev.arpan.imc.demo.data.UserRepository
import dev.arpan.imc.demo.data.local.db.AppDatabase
import dev.arpan.imc.demo.prefs.PreferenceStorage
import dev.arpan.imc.demo.prefs.SharedPreferenceStorage
import dev.arpan.imc.demo.ui.MainViewModel
import dev.arpan.imc.demo.ui.home.HomeViewModel
import dev.arpan.imc.demo.ui.login.LoginViewModel
import dev.arpan.imc.demo.ui.profile.ProfileViewModel
import dev.arpan.imc.demo.ui.signup.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

val appModule = module {

    fun provideAppDatabase(applicationContext: Context) =
        AppDatabase.getInstance(applicationContext)

    fun providePreferenceStorage(applicationContext: Context): PreferenceStorage =
        SharedPreferenceStorage(applicationContext)

    single {
        provideAppDatabase(get())
    }

    single {
        providePreferenceStorage(get())
    }
}

val repoModule = module {
    fun provideUserRepository(appDatabase: AppDatabase): UserRepository =
        DefaultUserRepository(appDatabase)

    single {
        provideUserRepository(get())
    }
}

val viewModelModule = module {

    viewModel {
        MainViewModel(get())
    }

    viewModel {
        LoginViewModel(get(), get())
    }

    viewModel {
        SignUpViewModel(get())
    }

    viewModel {
        HomeViewModel()
    }

    viewModel {
        ProfileViewModel(get())
    }
}

fun Application.initKoin() {
    startKoin {
        androidContext(this@initKoin)
        if (BuildConfig.DEBUG) {
            androidLogger(Level.DEBUG)
        }
        modules(
            listOf(
                appModule,
                repoModule,
                viewModelModule
            )
        )
    }
}
