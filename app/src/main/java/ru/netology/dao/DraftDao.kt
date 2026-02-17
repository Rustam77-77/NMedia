package ru.netology.dao
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.data.Draft
import ru.netology.db.AppDbHelper
class DraftDao(private val dbHelper: AppDbHelper) {
    fun get(): Draft? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            AppDbHelper.TABLE_DRAFTS,
            null,
            "${AppDbHelper.COLUMN_DRAFT_ID} = ?",
            arrayOf("1"),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.moveToFirst()) mapCursorToDraft(it) else null
        }
    }
    fun save(draft: Draft) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AppDbHelper.COLUMN_DRAFT_ID, draft.id)
            put(AppDbHelper.COLUMN_DRAFT_CONTENT, draft.content)
            put(AppDbHelper.COLUMN_DRAFT_SAVED_AT, draft.savedAt)
        }
        db.insertWithOnConflict(
            AppDbHelper.TABLE_DRAFTS,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
    }
    fun clear() {
        val db = dbHelper.writableDatabase
        db.delete(
            AppDbHelper.TABLE_DRAFTS,
            "${AppDbHelper.COLUMN_DRAFT_ID} = ?",
            arrayOf("1")
        )
    }
    private fun mapCursorToDraft(cursor: Cursor): Draft {
        return Draft(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDbHelper.COLUMN_DRAFT_ID)),
            content = cursor.getString(cursor.getColumnIndexOrThrow(AppDbHelper.COLUMN_DRAFT_CONTENT)),
            savedAt = cursor.getLong(cursor.getColumnIndexOrThrow(AppDbHelper.COLUMN_DRAFT_SAVED_AT))
        )
    }
}