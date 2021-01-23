package dev.arpan.imc.demo.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.arpan.imc.demo.data.model.User

@Dao
interface UsersDao {
    @Insert
    suspend fun saveUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?
}
