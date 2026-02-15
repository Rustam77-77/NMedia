package ru.netology.dao
import androidx.lifecycle.LiveData
import androidx.room.*
import ru.netology.data.Draft
@Dao
interface DraftDao {

    @Query("SELECT * FROM drafts WHERE id = 1")
    fun get(): LiveData<Draft?>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(draft: Draft)
    @Query("DELETE FROM drafts WHERE id = 1")
    suspend fun clear()
}