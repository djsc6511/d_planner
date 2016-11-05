package planner.com.d_planner.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by soongu on 2016-11-02.
 */

public class CourseTableDBAdapter {
    public static final String KEY_NUM = "num";
    public static final String KEY_TITLE = "title";
    public static final String KEY_TEACHER = "teacher";
    public static final String KEY_TIME = "time";
    public static final String KEY_ROOM = "room";
    public static final String KEY_ROWID = "_id";


    public static final String KEY_COURSE_TITLE = "title";
    public static final String KEY_NOTE = "note";
    public static final String KEY_DATE = "date";
    public static final String KEY_NOTE_TITLE = "note_title";
    public static final String KEY_NOTE_CONTENT = "note_content";
    public static final String KEY_NOTE_ROWID = "_id";


    public static final String KEY_FRD_STD_ID = "std_id";
    public static final String KEY_FRD_C_NUM = "num";
    public static final String KEY_FRD_TITLE = "title";
    public static final String KEY_FRD_TEACHER = "teacher";
    public static final String KEY_FRD_TIME = "time";
    public static final String KEY_FRD_ROOM = "room";
    public static final String KEY_FRD_ROWID = "_id";


    private static final String TAG = "CourseTableDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    /** * * Database creation sql statement */
    private static final String DATABASE_CREATE_TIMETABLE = "create table timetable (_id integer primary key autoincrement, "
            + "num text not null, title text not null, teacher text not null, time text not null, room text not null);";
    private static final String DATABASE_CREATE_NOTES = "create table notes (_id integer primary key autoincrement, "
            + "title text not null, note text not null, date text not null, note_title text not null, note_content text not null);";
    private static final String DATABASE_CREATE_FRIENDS = "create table friends (_id integer primary key autoincrement, "
            + "std_id text not null, num text not null, title text not null, teacher text not null, time text not null, room text not null);";
    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE_TIMETABLE = "timetable";
    private static final String DATABASE_TABLE_NOTES = "notes";
    private static final String DATABASE_TABLE_FRIENDS = "friends";
    private static final int DATABASE_VERSION = 4;
    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_TIMETABLE);
            db.execSQL(DATABASE_CREATE_NOTES);
            db.execSQL(DATABASE_CREATE_FRIENDS);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS timetable");
            db.execSQL("DROP TABLE IF EXISTS notes");
            db.execSQL("DROP TABLE IF EXISTS friends");
            onCreate(db);
        }
    }

    public CourseTableDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public CourseTableDBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public long createCourse(String num, String title, String teacher,
                             String time, String room) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NUM, num);
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_TEACHER, teacher);
        initialValues.put(KEY_TIME, time);
        initialValues.put(KEY_ROOM, room);

        return mDb.insert(DATABASE_TABLE_TIMETABLE, null, initialValues);
    }
    public long createNote(String title, String note, String date,
                           String note_title, String note_content) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_COURSE_TITLE, title);
        initialValues.put(KEY_NOTE, note);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_NOTE_TITLE, note_title);
        initialValues.put(KEY_NOTE_CONTENT, note_content);

        return mDb.insert(DATABASE_TABLE_NOTES, null, initialValues);
    }
    public long createFriends(String std_id, String num, String title, String teacher,
                              String time, String room) {
        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_FRD_STD_ID, std_id);
        initialValues.put(KEY_FRD_C_NUM, num);
        initialValues.put(KEY_FRD_TITLE, title);
        initialValues.put(KEY_FRD_TEACHER, teacher);
        initialValues.put(KEY_FRD_TIME, time);
        initialValues.put(KEY_FRD_ROOM, room);

        return mDb.insert(DATABASE_TABLE_FRIENDS, null, initialValues);
    }

    public boolean deleteCourse(String rowId) {
        Log.i("Delete called", "value__" + rowId);
        return mDb.delete(DATABASE_TABLE_TIMETABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteNotes_Id(String rowId) {
        Log.i("Delete called", "value__" + rowId);
        return mDb.delete(DATABASE_TABLE_NOTES, KEY_ROWID + "=" + rowId, null) > 0;
    }
    public boolean deleteFriends_Id(String std_id) {
        Log.i("Delete called", "value__" + std_id);
        return mDb.delete(DATABASE_TABLE_FRIENDS, KEY_FRD_STD_ID + "=" + std_id, null) > 0;
    }

    public boolean deleteNotes(String course_title) {
        Log.i("Delete called", "value__" + course_title);
        return mDb.delete(DATABASE_TABLE_NOTES, KEY_COURSE_TITLE + "='" + course_title+"'", null) > 0;
    }

    public Cursor fetchAllCourse() {
        return mDb.query(DATABASE_TABLE_TIMETABLE, new String[] { KEY_ROWID, KEY_NUM,
                        KEY_TITLE, KEY_TEACHER, KEY_TIME, KEY_ROOM }, null, null, null,
                null, null);
    }
    public Cursor fetchAllNotes() {
        return mDb.query(DATABASE_TABLE_NOTES, new String[] { KEY_ROWID, KEY_COURSE_TITLE,
                        KEY_NOTE, KEY_DATE, KEY_NOTE_TITLE, KEY_NOTE_CONTENT }, null, null, null,
                null, null);
    }
    public Cursor fetchAllFriends() {
        return mDb.query(DATABASE_TABLE_FRIENDS, new String[] { KEY_FRD_ROWID, KEY_FRD_STD_ID, KEY_FRD_C_NUM,
                KEY_FRD_TITLE, KEY_FRD_TEACHER, KEY_FRD_TIME, KEY_FRD_ROOM }, null, null, null, null, null);
    }

    public Cursor fetchCourse(long rowId) throws SQLException {
        Cursor mCursor = mDb.query(true, DATABASE_TABLE_TIMETABLE, new String[] {
                        KEY_ROWID, KEY_NUM,
                        KEY_TITLE, KEY_TEACHER, KEY_TIME, KEY_ROOM }, KEY_ROWID + "=" + rowId,
                null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public Cursor fetchCourse_Title(String title) throws SQLException {
        Cursor mCursor = mDb.query(true, DATABASE_TABLE_TIMETABLE, new String[] {
                        KEY_ROWID, KEY_NUM,
                        KEY_TITLE, KEY_TEACHER, KEY_TIME, KEY_ROOM }, KEY_TITLE + "='" + title+"'",
                null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchNotes_ID(String rowId) throws SQLException {
        Cursor mCursor = mDb.query(true, DATABASE_TABLE_NOTES, new String[] {
                        KEY_ROWID, KEY_COURSE_TITLE,
                        KEY_NOTE, KEY_DATE, KEY_NOTE_TITLE, KEY_NOTE_CONTENT }, KEY_ROWID + "=" + rowId,
                null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public Cursor fetchNotes_CourseTitle(String course_title) throws SQLException {
        Cursor mCursor = mDb.query(true, DATABASE_TABLE_NOTES, new String[] {
                        KEY_ROWID, KEY_COURSE_TITLE,
                        KEY_NOTE, KEY_DATE, KEY_NOTE_TITLE, KEY_NOTE_CONTENT }, KEY_COURSE_TITLE + "='" + course_title+"'",
                null, null, null, KEY_DATE + " DESC", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchFriends_Id(String std_id) throws SQLException {
        Cursor mCursor = mDb.query(true, DATABASE_TABLE_FRIENDS, new String[] {
                        KEY_FRD_ROWID, KEY_FRD_STD_ID, KEY_FRD_C_NUM,
                        KEY_FRD_TITLE, KEY_FRD_TEACHER, KEY_FRD_TIME, KEY_FRD_ROOM }, KEY_FRD_STD_ID + "='" + std_id +"'",
                null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public boolean updateCourse(String rowId, String num, String title,
                                String teacher, String time, String room) {
        ContentValues args = new ContentValues();
        args.put(KEY_NUM, num);
        args.put(KEY_TITLE, title);
        args.put(KEY_TEACHER, teacher);
        args.put(KEY_TIME, time);
        args.put(KEY_ROOM, room);
        return mDb.update(DATABASE_TABLE_TIMETABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
    public boolean updateNotes(String rowId, String title, String note, String date,
                               String note_title, String note_content) {
        ContentValues args = new ContentValues();
        args.put(KEY_COURSE_TITLE, title);
        args.put(KEY_NOTE, note);
        args.put(KEY_DATE, date);
        args.put(KEY_NOTE_TITLE, note_title);
        args.put(KEY_NOTE_CONTENT, note_content);
        return mDb.update(DATABASE_TABLE_NOTES, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

}

