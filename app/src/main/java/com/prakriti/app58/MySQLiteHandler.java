package com.prakriti.app58;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTableLockedException;

import com.prakriti.app58.Computer;

import java.util.ArrayList;
import java.util.List;

public class MySQLiteHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "computer.db";
    private static final String TABLE_COMPUTER = "computers";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_COMPUTER_NAME = "computerName";
    private static final String COLUMN_COMPUTER_TYPE = "computerType";

    String CREATE_COMPUTER_TABLE = "CREATE TABLE " + TABLE_COMPUTER + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY, " + COLUMN_COMPUTER_NAME + " TEXT, " +
                    COLUMN_COMPUTER_TYPE + " TEXT)";

    public MySQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COMPUTER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String UPGRADE_COMPUTER_TABLE = "DROP TABLE IF EXISTS " + TABLE_COMPUTER;
        db.execSQL(UPGRADE_COMPUTER_TABLE);
        onCreate(db);
    }

    // DB Operations: create, read, update, delete

    public void addComputer(Computer computer) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_COMPUTER_NAME, computer.getComputerName());
        values.put(COLUMN_COMPUTER_TYPE, computer.getComputerType());

        database.insert(TABLE_COMPUTER, null, values);
        database.close();
    }

    public Computer getComputer(int id) {
        SQLiteDatabase database = getReadableDatabase();
            // Cursor is an interface that provides read-write access to data returned from a database
        Cursor cursor = database.query(TABLE_COMPUTER, new String[] {COLUMN_ID, COLUMN_COMPUTER_NAME, COLUMN_COMPUTER_TYPE},
                COLUMN_ID + "=?", new String[] {String.valueOf(id)}, null, null, null );

        if(cursor!=null)
            cursor.moveToFirst();

        Computer computer = new Computer(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        return computer;
    }

    public List<Computer> getAllComputers() {
        List<Computer> allComputers = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        String selectAllQuery = "SELECT * FROM " + TABLE_COMPUTER;
        Cursor cursor = database.rawQuery(selectAllQuery, null);

        if(cursor.moveToFirst()) {
            do {
                Computer computer = new Computer(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                allComputers.add(computer);
            } while (cursor.moveToNext());
        }
        return allComputers;
    }

    public int updateComputer(Computer computer) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_COMPUTER_NAME, computer.getComputerName());
        values.put(COLUMN_COMPUTER_TYPE, computer.getComputerType());

        return database.update(TABLE_COMPUTER, values, COLUMN_ID + "=?", new String[] {String.valueOf(computer.getId())});
    }

    public void deleteComputer(Computer computer) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_COMPUTER, COLUMN_ID + "=?", new String[] {String.valueOf(computer.getId())});
        database.close();
    }

    public int getComputerCount() {
        SQLiteDatabase database = getReadableDatabase();
        String computerCountQuery = "SELECT * FROM " + TABLE_COMPUTER;
        Cursor cursor = database.rawQuery(computerCountQuery, null);
        cursor.close();
        return cursor.getCount();
    }

}