package com.dani.makersinstitute.studentscore.utils;

/**
 * Created by dani@taufani.com on 1/30/17.
 */
public class Constants {
    public static final int REQUEST_ADD_SCORE = 11;
    public static final int REQUEST_EDIT_PROFILE = 12;
    public static final String KEY_ID = "id";

    // Shared Preferences
    public final static String KEY_SHARED_PREF = "pref";
    public final static String KEY_NAME = "name";
    public final static String KEY_MAJOR = "major";

    // Database
    public final static String DATABASE_NAME = "DB_STUDENT";
    public final static int DATABASE_VERSION = 1;
    public final static String TABLE_SUBJECT = "TB_SUBJECT";
    public final static String FIELD_ID = "id";
    public final static String FIELD_SUBJECT = "subject";
    public final static String FIELD_SCORE = "score";
    public final static String FIELD_DATE_CREATED = "created_at";
}
