package ru.netology.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.dao.DraftDao
import ru.netology.dao.PostDao
import ru.netology.data.Draft
import ru.netology.data.Post
@Database(
    entities = [Post::class, Draft::class],
    version = 1,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun draftDao(): DraftDao
    companion object {
        @Volatile
        private var instance: AppDb? = null
        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, "app.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}