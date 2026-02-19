package ru.netology.nmedia.db
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
class AppDbHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_POSTS_TABLE)
        db.execSQL(SQL_CREATE_DRAFTS_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_POSTS_TABLE)
        db.execSQL(SQL_DELETE_DRAFTS_TABLE)
        onCreate(db)
    }
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "netology.db"
        // Таблица постов
        const val TABLE_POSTS = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PUBLISHED = "published"
        const val COLUMN_LIKED_BY_ME = "likedByMe"
        const val COLUMN_LIKES = "likes"
        const val COLUMN_SHARES = "shares"
        const val COLUMN_VIEWS = "views"
        const val COLUMN_VIDEO_URL = "videoUrl"
        private const val SQL_CREATE_POSTS_TABLE = """
            CREATE TABLE $TABLE_POSTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_AUTHOR TEXT NOT NULL,
                $COLUMN_CONTENT TEXT NOT NULL,
                $COLUMN_PUBLISHED INTEGER NOT NULL,
                $COLUMN_LIKED_BY_ME INTEGER NOT NULL DEFAULT 0,
                $COLUMN_LIKES INTEGER NOT NULL DEFAULT 0,
                $COLUMN_SHARES INTEGER NOT NULL DEFAULT 0,
                $COLUMN_VIEWS INTEGER NOT NULL DEFAULT 0,
                $COLUMN_VIDEO_URL TEXT
            )
        """
        private const val SQL_DELETE_POSTS_TABLE = "DROP TABLE IF EXISTS $TABLE_POSTS"
        // Таблица черновиков
        const val TABLE_DRAFTS = "drafts"
        const val COLUMN_DRAFT_ID = "id"
        const val COLUMN_DRAFT_CONTENT = "content"
        const val COLUMN_DRAFT_SAVED_AT = "savedAt"
        private const val SQL_CREATE_DRAFTS_TABLE = """
            CREATE TABLE $TABLE_DRAFTS (
                $COLUMN_DRAFT_ID INTEGER PRIMARY KEY DEFAULT 1,
                $COLUMN_DRAFT_CONTENT TEXT NOT NULL,
                $COLUMN_DRAFT_SAVED_AT INTEGER NOT NULL
            )
        """
        private const val SQL_DELETE_DRAFTS_TABLE = "DROP TABLE IF EXISTS $TABLE_DRAFTS"
    }
}