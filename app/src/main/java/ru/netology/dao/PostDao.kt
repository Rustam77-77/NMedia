package ru.netology.dao
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.data.Post
import ru.netology.db.AppDbHelper
class PostDao(private val dbHelper: AppDbHelper) {
    fun getAll(): List<Post> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            AppDbHelper.TABLE_POSTS,
            null,
            null,
            null,
            null,
            null,
            "${AppDbHelper.COLUMN_ID} DESC"
        )
        return cursor.use { mapCursorToList(it) }
    }
    fun getById(id: Long): Post? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            AppDbHelper.TABLE_POSTS,
            null,
            "${AppDbHelper.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.moveToFirst()) mapCursorToPost(it) else null
        }
    }
    fun insert(post: Post): Long {
        val db = dbHelper.writableDatabase
        val values = postToContentValues(post)
        return db.insert(AppDbHelper.TABLE_POSTS, null, values)
    }
    fun update(post: Post) {
        val db = dbHelper.writableDatabase
        val values = postToContentValues(post)
        db.update(
            AppDbHelper.TABLE_POSTS,
            values,
            "${AppDbHelper.COLUMN_ID} = ?",
            arrayOf(post.id.toString())
        )
    }
    fun removeById(id: Long) {
        val db = dbHelper.writableDatabase
        db.delete(
            AppDbHelper.TABLE_POSTS,
            "${AppDbHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }
    fun likeById(id: Long) {
        val db = dbHelper.writableDatabase
        db.execSQL(
            "UPDATE ${AppDbHelper.TABLE_POSTS} SET ${AppDbHelper.COLUMN_LIKES} = ${AppDbHelper.COLUMN_LIKES} + 1, ${AppDbHelper.COLUMN_LIKED_BY_ME} = 1 WHERE ${AppDbHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }
    fun unlikeById(id: Long) {
        val db = dbHelper.writableDatabase
        db.execSQL(
            "UPDATE ${AppDbHelper.TABLE_POSTS} SET ${AppDbHelper.COLUMN_LIKES} = ${AppDbHelper.COLUMN_LIKES} - 1, ${AppDbHelper.COLUMN_LIKED_BY_ME} = 0 WHERE ${AppDbHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }
    fun shareById(id: Long) {
        val db = dbHelper.writableDatabase
        db.execSQL(
            "UPDATE ${AppDbHelper.TABLE_POSTS} SET ${AppDbHelper.COLUMN_SHARES} = ${AppDbHelper.COLUMN_SHARES} + 1 WHERE ${AppDbHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }
    private fun postToContentValues(post: Post): ContentValues {
        return ContentValues().apply {
            if (post.id != 0L) put(AppDbHelper.COLUMN_ID, post.id)
            put(AppDbHelper.COLUMN_AUTHOR, post.author)
            put(AppDbHelper.COLUMN_CONTENT, post.content)
            put(AppDbHelper.COLUMN_PUBLISHED, post.published)
            put(AppDbHelper.COLUMN_LIKED_BY_ME, if (post.likedByMe) 1 else 0)
            put(AppDbHelper.COLUMN_LIKES, post.likes)
            put(AppDbHelper.COLUMN_SHARES, post.shares)
            put(AppDbHelper.COLUMN_VIEWS, post.views)
            put(AppDbHelper.COLUMN_VIDEO_URL, post.videoUrl)
        }
    }
    private fun mapCursorToPost(cursor: Cursor): Post {
        return Post(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(AppDbHelper.COLUMN_ID)),
            author = cursor.getString(cursor.getColumnIndexOrThrow(AppDbHelper.COLUMN_AUTHOR)),
            content = cursor.getString(cursor.getColumnIndexOrThrow(AppDbHelper.COLUMN_CONTENT)),
            published = cursor.getLong(cursor.getColumnIndexOrThrow(AppDbHelper.COLUMN_PUBLISHED)),
            likedByMe = cursor.getInt(cursor.getColumnIndexOrThrow(AppDbHelper.COLUMN_LIKED_BY_ME)) == 1,
            likes = cursor.getInt(cursor.getColumnIndexOrThrow(AppDbHelper.COLUMN_LIKES)),
            shares = cursor.getInt(cursor.getColumnIndexOrThrow(AppDbHelper.COLUMN_SHARES)),
            views = cursor.getInt(cursor.getColumnIndexOrThrow(AppDbHelper.COLUMN_VIEWS)),
            videoUrl = cursor.getString(cursor.getColumnIndexOrThrow(AppDbHelper.COLUMN_VIDEO_URL))
        )
    }
    private fun mapCursorToList(cursor: Cursor): List<Post> {
        val posts = mutableListOf<Post>()
        while (cursor.moveToNext()) {
            posts.add(mapCursorToPost(cursor))
        }
        return posts
    }
}