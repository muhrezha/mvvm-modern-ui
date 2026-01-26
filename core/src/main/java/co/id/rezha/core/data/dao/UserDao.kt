package co.id.rezha.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import co.id.rezha.core.data.tables.TableUser
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    fun insert(tableUser: TableUser)

    @Insert
    fun insertAll(tableUsers: List<TableUser>)

    @Query("SELECT * FROM user WHERE id = :id LIMIT 1")
    fun getUserById(id: Int): TableUser?

    @Query("SELECT * FROM user")
    fun getAllUser(): List<TableUser>

    @Update
    fun update(user: TableUser)
}