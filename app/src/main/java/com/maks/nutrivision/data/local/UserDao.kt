package com.maks.nutrivision.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maks.nutrivision.data.entities.Profile

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: Profile)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<Profile>
}