package dev.arpan.imc.demo.data

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dev.arpan.imc.demo.MOCK_USER
import dev.arpan.imc.demo.data.local.db.AppDatabase
import dev.arpan.imc.demo.data.local.db.UsersDao
import dev.arpan.imc.demo.data.model.ResultWrapper
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class DefaultUserRepositoryTest {

    private val usersDao: UsersDao = mock {
        runBlocking {
            doReturn(Unit).whenever(mock).saveUser(MOCK_USER)
            doReturn(MOCK_USER).whenever(mock).getUserByEmail(any())
        }
    }
    private val appDatabase: AppDatabase = mock {
        whenever(mock.usersDao).thenReturn(usersDao)
    }

    @Test
    fun `register() returns success if successful`() = runBlockingTest {
        doReturn(Unit).whenever(usersDao).saveUser(MOCK_USER)
        val userRepository = DefaultUserRepository(appDatabase, TestCoroutineDispatcher())
        assertThat(userRepository.register(MOCK_USER)).isInstanceOf(ResultWrapper.Success::class.java)
    }

/*
    @Test
    fun `register() returns false if error`() = runBlockingTest {
      //  doReturn(Throwable()).whenever(usersDao).saveUser(MOCK_USER)
        val userRepository = DefaultUserRepository(appDatabase, TestCoroutineDispatcher())
        whenever(usersDao.saveUser(MOCK_USER)).thenReturn(null)
        print(userRepository.register(MOCK_USER))
        //assertThat(userRepository.register(MOCK_USER)).isEqualTo(RegisterResult.Error)
    }
*/

    @Test
    fun `login() returns User success if correct credential`() = runBlockingTest {
        val userRepository = DefaultUserRepository(appDatabase, TestCoroutineDispatcher())
        val user = userRepository.login(MOCK_USER.email, MOCK_USER.password)
        assertThat(user).isEqualTo(MOCK_USER)
    }

    @Test
    fun `login() returns null if password mismatch`() = runBlockingTest {
        val userRepository = DefaultUserRepository(appDatabase, TestCoroutineDispatcher())
        val user = userRepository.login(MOCK_USER.email, "1234")
        assertThat(user).isNull()
    }
}
