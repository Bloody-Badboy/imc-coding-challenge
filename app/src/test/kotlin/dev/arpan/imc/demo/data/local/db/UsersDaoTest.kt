package dev.arpan.imc.demo.data.local.db

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dev.arpan.imc.demo.MOCK_USER
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UsersDaoTest {

    private lateinit var appDatabase: AppDatabase

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDatabase() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDatabase() {
        appDatabase.close()
    }

    @Test
    fun `user insert and get by email`() = runBlockingTest {
        val usersDao = appDatabase.usersDao
        usersDao.saveUser(MOCK_USER)
        val user = usersDao.getUserByEmail(MOCK_USER.email)
        assertThat(user).isEqualTo(MOCK_USER)
    }

    @Test
    fun `returns null if user not found by email`() = runBlockingTest {
        val usersDao = appDatabase.usersDao
        usersDao.saveUser(MOCK_USER)
        val user = usersDao.getUserByEmail("unknown@email.com")
        assertThat(user).isNull()
    }

    @Test(expected = SQLiteConstraintException::class)
    fun `duplicate email not allowed`() = runBlockingTest {
        val usersDao = appDatabase.usersDao
        usersDao.saveUser(MOCK_USER)
        usersDao.saveUser(MOCK_USER)
    }
}
