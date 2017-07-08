package org.suai.zabik.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.LinkedList;


public class DataBase extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "LastRequest";
    private static final String KEY_REQUEST = "request"; //contain full request which made by user
    private static final String KEY_REQUEST_ANSWER = "requestAnswer";
    private static final String KEY_REQEST_ANSWER_LANGS = "langs";
    //include text, result of translation and langs
    private static final String IS_FAVORITE = "favorite";
    private static final String KEY_ID = "_id";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Translate";
    //private static Context context;

    private DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static public ArrayList<History> getAll(Context context) {
        DataBase database = new DataBase(context);
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ DataBase.TABLE_NAME + " ORDER BY " + DataBase.KEY_ID + " DESC LIMIT 100",null);
        ArrayList<History> res = new ArrayList<>();
        if(cursor.moveToFirst()){
            int idx = cursor.getColumnIndex(DataBase.KEY_ID);
            int request = cursor.getColumnIndex(DataBase.KEY_REQUEST);
            int answer = cursor.getColumnIndex(DataBase.KEY_REQUEST_ANSWER);
            int isFavorite = cursor.getColumnIndex(DataBase.IS_FAVORITE);
            int langs = cursor.getColumnIndex(DataBase.KEY_REQEST_ANSWER_LANGS);

            do{
                History tmp = new History();
                boolean isFav = false;
                if(cursor.getInt(isFavorite) == 1){
                    isFav = true;
                }
                tmp.setFavorite(isFav);
                tmp.setLanguage(cursor.getString(langs));
                tmp.setResponse(cursor.getString(answer));
                tmp.setRequest(cursor.getString(request));
                tmp.setId(cursor.getInt(idx));
                res.add(tmp);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return res;
    }
    static public ArrayList<History> getAllFavorite(Context context) {
        DataBase database = new DataBase(context);
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ DataBase.TABLE_NAME + " ORDER BY " + DataBase.KEY_ID + " DESC LIMIT 100",null);
        ArrayList<History> res = new ArrayList<>();
        if(cursor.moveToFirst()){
            int idx = cursor.getColumnIndex(DataBase.KEY_ID);
            int request = cursor.getColumnIndex(DataBase.KEY_REQUEST);
            int answer = cursor.getColumnIndex(DataBase.KEY_REQUEST_ANSWER);
            int isFavorite = cursor.getColumnIndex(DataBase.IS_FAVORITE);
            int langs = cursor.getColumnIndex(DataBase.KEY_REQEST_ANSWER_LANGS);

            do{
                History tmp = new History();
                boolean isFav = false;
                if(cursor.getInt(isFavorite) == 1){
                    isFav = true;
                }
                tmp.setFavorite(isFav);
                tmp.setLanguage(cursor.getString(langs));
                tmp.setResponse(cursor.getString(answer));
                tmp.setRequest(cursor.getString(request));
                tmp.setId(cursor.getInt(idx));
                if (tmp.getIsFavlorite()){
                    res.add(tmp);
                }

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return res;
    }

    //return new state of element
    public static boolean changeFavoriteState(Context context, long itemId) {
        DataBase dataBase = new DataBase(context);
        SQLiteDatabase db = dataBase.getWritableDatabase();
        Cursor data = db.rawQuery("Select " + DataBase.IS_FAVORITE + " from " + DataBase.TABLE_NAME + " where " + DataBase.KEY_ID + " =" + itemId, null);
        data.moveToFirst();
        boolean returnValue;
        int res = data.getInt(data.getColumnIndex(DataBase.IS_FAVORITE));
        if (res == 0) {
            db.execSQL("UPDATE " + DataBase.TABLE_NAME + " SET " + DataBase.IS_FAVORITE + "=" + 1 + " where " + DataBase.KEY_ID + "=" + itemId);
            returnValue = true;
        } else {
            db.execSQL("UPDATE " + DataBase.TABLE_NAME + " SET " + DataBase.IS_FAVORITE + "=" + 0 + " where " + DataBase.KEY_ID + "=" + itemId);
            returnValue = false;
        }
        data.close();
        db.close();
        return returnValue;

    }

    public static void deleteDataBase(Context context) {
        DataBase dataBase = new DataBase(context);
        SQLiteDatabase sqLiteDatabase = dataBase.getWritableDatabase();
        sqLiteDatabase.delete(DataBase.TABLE_NAME, null, null);
        dataBase.close();
    }

    public static void save(Context context, String request, String result, String langs) {
        DataBase database = new DataBase(context);
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBase.TABLE_NAME + " WHERE " + DataBase.KEY_REQUEST + " = " + "'" + request + "'" + " AND " + DataBase.KEY_REQEST_ANSWER_LANGS + " = " + "'" + langs + "'", null);
        if (cursor.moveToFirst()) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBase.KEY_REQUEST, request);
        contentValues.put(DataBase.KEY_REQUEST_ANSWER, result);
        contentValues.put(DataBase.KEY_REQEST_ANSWER_LANGS, langs);
        db.insert(DataBase.TABLE_NAME, null, contentValues);
        cursor.close();
        db.close();
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + KEY_REQUEST + " string, "
                + KEY_REQUEST_ANSWER + " string, "
                + KEY_REQEST_ANSWER_LANGS + " string, "
                + IS_FAVORITE + " integer "
                + ")"
        );
    }

    public void changeIsFavorite(Context context, int id, int favorite) {
        DataBase database = new DataBase(context);
        SQLiteDatabase db = database.getWritableDatabase();
        db.execSQL("UPDATE " + DataBase.TABLE_NAME + " SET " + DataBase.IS_FAVORITE + "=" + favorite + " where " + DataBase.KEY_ID + " = " + id);
        db.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}
