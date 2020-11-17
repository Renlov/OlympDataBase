package com.example.olimpdb.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.olimpdb.Data.ClubOlympusContract.MemberEntry;

public class OlympusDbOpenHelper extends SQLiteOpenHelper {

    public OlympusDbOpenHelper(Context context) {
        super(context, MemberEntry.DATABASE_NAME,
                null,
                MemberEntry.DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_MEMBERS = "CREATE TABLE " + MemberEntry.TABLE_NAME + "("
                + MemberEntry.KEY_ID + " INTEGER PRIMARY KEY,"
                + MemberEntry.KEY_FIRST_NAME + " TEXT,"
                + MemberEntry.KEY_LAST_NAME + " TEXT,"
                + MemberEntry.KEY_GENDER + " INTEGER NOT NULL,"
                + MemberEntry.KEY_SPORT + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_MEMBERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MemberEntry.TABLE_NAME);
        onCreate(db);
    }
}
