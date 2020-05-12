package com.example.jugal.sqlite;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NoteTable {
    static final String TABLENAME = "todotasks";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_TASK = "task";
    static final String COLUMN_PRIORITY = "priority";
    static final String COLUMN_STATUS = "status";
    static final String COLUMN_TIME = "time";
    static final String COLUMN_POSITION = "position";

    static public void onCreate(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE "+ TABLENAME + " (");
        sb.append(COLUMN_ID +" INTEGER PRIMARY KEY," );
        sb.append(COLUMN_TASK +" TEXT, ");
        sb.append(COLUMN_PRIORITY +" TEXT, ");
        sb.append(COLUMN_STATUS +" INT, ");
        sb.append(COLUMN_TIME +" TEXT, ");
        sb.append(COLUMN_POSITION +" INT);");
        Log.d("a",""+sb.toString());
        try{
            db.execSQL(sb.toString());
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    static public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLENAME);
        NoteTable.onCreate(db);
    }

}
