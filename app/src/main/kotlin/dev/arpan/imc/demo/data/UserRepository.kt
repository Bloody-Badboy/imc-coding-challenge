package dev.arpan.imc.demo.data

import dev.arpan.imc.demo.data.local.db.AppDatabase
import dev.arpan.imc.demo.data.model.ResultWrapper
import dev.arpan.imc.demo.data.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface UserRepository {

    suspend fun register(user: User): ResultWrapper<Unit>

    suspend fun login(email: String, password: String): User?

    suspend fun getUserDetails(email: String): User?
}

class DefaultUserRepository(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {
    override suspend fun register(user: User): ResultWrapper<Unit> {
        return withContext(dispatcher) {
            try {
                appDatabase.usersDao.saveUser(user)
                return@withContext ResultWrapper.Success(Unit)
            } catch (t: Throwable) {
                t.printStackTrace()
                return@withContext ResultWrapper.Error(t)
            }
        }
    }

    override suspend fun login(email: String, password: String): User? {
        return withContext(dispatcher) {
            appDatabase.usersDao.getUserByEmail(email)?.let { user ->
                if (user.password == password) {
                    user
                } else {
                    null
                }
            }
        }
    }

    override suspend fun getUserDetails(email: String): User? {
        return withContext(dispatcher) {
            appDatabase.usersDao.getUserByEmail(email)
        }
    }
}
