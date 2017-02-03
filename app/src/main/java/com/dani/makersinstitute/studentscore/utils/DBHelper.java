package com.dani.makersinstitute.studentscore.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dani.makersinstitute.studentscore.model.ScoreModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dani@taufani.com on 1/31/17.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Constants.TABLE_SUBJECT + " (" +
                Constants.FIELD_ID + " integer primary key autoincrement, " +
                Constants.FIELD_SUBJECT + " varchar(100) not null, " +
                Constants.FIELD_SCORE + " integer not null, " +
                Constants.FIELD_DATE_CREATED + " datetime);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_SUBJECT + ";");

        onCreate(db);
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * get datetime
     * */
    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    // ------------------------ "SUBJECT" table methods ----------------//

    public void insertSubject(String subject, Double score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.FIELD_SUBJECT, subject);
        values.put(Constants.FIELD_SCORE, score);
        values.put(Constants.FIELD_DATE_CREATED, getDateTime());
        db.insert(Constants.TABLE_SUBJECT, null, values);
        db.close();
    }

    public int updateSubject(ScoreModel score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.FIELD_SUBJECT, score.getSubjectName());
        values.put(Constants.FIELD_SCORE, score.getSubjectScore());

        // updating row
        return db.update(Constants.TABLE_SUBJECT, values, Constants.FIELD_ID + " = ?",
                new String[] { String.valueOf(score.getId()) });
    }

    public void deleteSubject(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_SUBJECT, Constants.FIELD_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public ArrayList<ScoreModel> getSubject() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ScoreModel> scores = new ArrayList<>();
        Cursor cursor = db.query(Constants.TABLE_SUBJECT, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ScoreModel event = new ScoreModel(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2));

                scores.add(event);
            }
            while (cursor.moveToNext());
        }
        return scores;
    }

    public ScoreModel getSubject(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + Constants.TABLE_SUBJECT + " WHERE "
                + Constants.FIELD_ID + " = " + id, null);

        if (c != null)
            c.moveToFirst();

        ScoreModel score = new ScoreModel();
        score.setId(c.getInt(c.getColumnIndex(Constants.FIELD_ID)));
        score.setSubjectName((c.getString(c.getColumnIndex(Constants.FIELD_SUBJECT))));
        score.setSubjectScore(c.getDouble(c.getColumnIndex(Constants.FIELD_SCORE)));

        return score;
    }

    public boolean isDuplicated(String text) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT count(*) FROM " + Constants.TABLE_SUBJECT
                    + " WHERE UPPER(" + Constants.FIELD_SUBJECT + ") =?", new String[]{text});
            if (cursor != null)
                cursor.moveToFirst();

            if (Integer.parseInt(cursor.getString(0)) > 0)
                return true;
        } finally {
            cursor.close();
        }
        return false;
    }
}
