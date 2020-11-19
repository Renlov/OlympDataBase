package com.example.olimpdb.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.olimpdb.Data.ClubOlympusContract.*;

public class OlympusContentProvider extends ContentProvider {

    OlympusDbOpenHelper dbOpenHelper;

    private static final int MEMBERS = 111;
    private static final int MEMBER_ID = 222;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(ClubOlympusContract.MemberEntry.AUTHORITY, ClubOlympusContract.MemberEntry.PATH_MEMBERS, MEMBERS);
        uriMatcher.addURI(ClubOlympusContract.MemberEntry.AUTHORITY, ClubOlympusContract.MemberEntry.PATH_MEMBERS + "/#", MEMBER_ID);
    }

    @Override
    public boolean onCreate() {
        dbOpenHelper = new OlympusDbOpenHelper(getContext());
        return true;
    }

    @Override
    //Запрашивает данные из таблицы
    //URI - идентификатор ресурса
    //content://com.android.example.olimpdb/members/34
    //projection - имена столбцов {"lastName", "gender"}
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor;

        int match = uriMatcher.match(uri);

        switch (match){
            case MEMBERS:
                //Работаем со всеми таблицами
                cursor = db.query(MemberEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null,
                        sortOrder);
                break;

            case MEMBER_ID:
                //Selection - отбор по ID
                //SelectionArgs - аргументы для отбора (34)
                selection = MemberEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                //в cursor передаем то, что хотим получить
                cursor = db.query(MemberEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            default:
                //В случае некорректного URI
                    throw new IllegalArgumentException("Can't query incorrect URI" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

       @Override
       //URI - идентификатор ресурса
       //content://com.android.example.olimpdb/members - запрос делаем такой,
       //по итогу получаем content://com.android.example.olimpdb/members/34
    public Uri insert(Uri uri, ContentValues values) {

        String firstName = values.getAsString(MemberEntry.KEY_FIRST_NAME);
        if(firstName == null) {
            throw new IllegalArgumentException("You have to input first name");
        }

        String lastName = values.getAsString(MemberEntry.KEY_LAST_NAME);
        if(lastName == null) {
            throw new IllegalArgumentException("You have to input last name");
        }

        Integer gender = values.getAsInteger(MemberEntry.KEY_GENDER);
        if(gender == null || !(gender == MemberEntry.GENDER_UNKNOWN
        || gender ==MemberEntry.GENDER_MALE || gender == MemberEntry.GENDER_FEMALE)){
            throw new IllegalArgumentException("You have to input correct gender");
        }
        String sport = values.getAsString(MemberEntry.KEY_SPORT);
        if(sport == null) {
            throw new IllegalArgumentException("You have to input sport");
        }



        SQLiteDatabase database  = dbOpenHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
           switch (match){
               case MEMBERS:
                   long id = database.insert(MemberEntry.TABLE_NAME, null, values);
                   if(id == -1){
                       Log.e("insertMethod", "Insertion of data the table failed for " + uri);
                       return null;
                   }
                   getContext().getContentResolver().notifyChange(uri, null);
                   return ContentUris.withAppendedId(uri, id);

               default:
                   //В случае некорректного URI
                   throw new IllegalArgumentException("Can't query incorrect URI" + uri);
           }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int rowsDeleted;
        switch (match){

            case MEMBERS:
                rowsDeleted = db.delete(MemberEntry.TABLE_NAME, selection , selectionArgs);
                break;

            case MEMBER_ID:
                selection = MemberEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(MemberEntry.TABLE_NAME, selection , selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Can't delete this URI" + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);

        }
        return rowsDeleted;
    }

    @Override
    //Возвращает количество строк, которые мы обновили
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if(values.containsKey(MemberEntry.KEY_FIRST_NAME)) {
            String firstName = values.getAsString(MemberEntry.KEY_FIRST_NAME);
            if (firstName == null) {
                throw new IllegalArgumentException("You have to input first name");
            }
        }
        if(values.containsKey(MemberEntry.KEY_LAST_NAME)) {
            String lastName = values.getAsString(MemberEntry.KEY_LAST_NAME);
            if (lastName == null) {
                throw new IllegalArgumentException("You have to input last name");
            }
        }
        if(values.containsKey(MemberEntry.KEY_GENDER)) {
            Integer gender = values.getAsInteger(MemberEntry.KEY_GENDER);
            if (gender == null || !(gender == MemberEntry.GENDER_UNKNOWN
                    || gender == MemberEntry.GENDER_MALE || gender == MemberEntry.GENDER_FEMALE)) {
                throw new IllegalArgumentException("You have to input correct gender");
            }
        }
        if(values.containsKey(MemberEntry.KEY_SPORT)) {
            String sport = values.getAsString(MemberEntry.KEY_SPORT);
            if (sport == null) {
                throw new IllegalArgumentException("You have to input sport");
            }
        }

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int rowsUpdated;
        switch (match){

            case MEMBERS:
                rowsUpdated =  db.update(MemberEntry.TABLE_NAME, values, selection , selectionArgs);

                break;
            case MEMBER_ID:
                selection = MemberEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated =  db.update(MemberEntry.TABLE_NAME, values, selection , selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Can't update this URI" + uri);
        }
        if(rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
            return rowsUpdated;
    }

    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match){

            case MEMBERS:
                return MemberEntry.CONTENT_MULTIPLE_ITEMS;

            case MEMBER_ID:

                return MemberEntry.CONTENT_SINGLE_ITEM;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
