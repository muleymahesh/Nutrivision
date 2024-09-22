package com.maks.nutrivision.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maks.nutrivision.data.entities.Profile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: Profile)

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<Profile>>
}