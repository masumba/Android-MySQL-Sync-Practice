package com.javarank.syncpractice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anik on 3/10/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private SQLiteDatabase database;
    private Cursor cursor;

    private static final String CREATE_TABLE_PERSON = "create table "+DBContract.TABLE_NAME+" ("+DBContract.ID+" integer primary key autoincrement, "+DBContract.FIRST_NAME+" text, "+
            DBContract.LAST_NAME+" text, "+DBContract.SYNC_STATUS+" integer); ";

    private static final String DROP_TABLE_PERSONS = "drop table if exists "+DBContract.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DBContract.DATABASE_NAME, null, DB_VERSION);
        this.database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_PERSON);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_PERSONS);
        onCreate(sqLiteDatabase);
    }


    public void saveToLocalDb(Person person, int status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.FIRST_NAME, person.getFirstName());
        contentValues.put(DBContract.LAST_NAME, person.getLastName());
        contentValues.put(DBContract.SYNC_STATUS, status);
        database.insert(DBContract.TABLE_NAME,  null, contentValues);
    }

    public List<Person> getPersonList() {
        ArrayList<Person> personList = new ArrayList<>();
        String[] projection = {DBContract.ID, DBContract.FIRST_NAME, DBContract.LAST_NAME, DBContract.SYNC_STATUS};
        Cursor cursor = database.query(DBContract.TABLE_NAME, projection, null, null, null, null, null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(DBContract.ID));
            String firstName = cursor.getString(cursor.getColumnIndex(DBContract.FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(DBContract.LAST_NAME));
            int status = cursor.getInt(cursor.getColumnIndex(DBContract.SYNC_STATUS));
            Person person = new Person(firstName, lastName, status);
            personList.add(person);
        }
        cursor.close();
        return personList;
    }

    public void updatePerson(Person person, int status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.SYNC_STATUS, status);
        String query = DBContract.FIRST_NAME+" LIKE ?";
        String[] selectionArgs = {person.getFirstName()};
        database.update(DBContract.TABLE_NAME, contentValues, query, selectionArgs);
    }

    public List<Person> getUnSyncedPersonList() {
        ArrayList<Person> personList = new ArrayList<>();
        String [] settingsProjection = {DBContract.ID, DBContract.FIRST_NAME, DBContract.LAST_NAME};
        String whereClause = DBContract.ID+"=?";
        String [] whereArgs = {"1"};
        cursor = database.query(DBContract.TABLE_NAME, settingsProjection, whereClause, whereArgs, null, null, null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(DBContract.ID));
            String firstName = cursor.getString(cursor.getColumnIndex(DBContract.FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(DBContract.LAST_NAME));
            //int status = cursor.getInt(cursor.getColumnIndex(DBContract.SYNC_STATUS));
            int status = DBContract.SYNC_STATUS_FAILED;
            Person person = new Person(id,firstName, lastName, status);
            personList.add(person);
        }
        cursor.close();
        return personList;
    }


}
