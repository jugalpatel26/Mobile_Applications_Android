package com.example.jugal.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseDataManager {
    private Context mContext;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private NotesDOA notesDOA;

    public DatabaseDataManager(Context mContext){
        this.mContext = mContext;
        databaseHelper = new DatabaseHelper(this.mContext);
        db = databaseHelper.getWritableDatabase();
        notesDOA = new NotesDOA(db);
    }

    public void close(){
        if(db!=null){
            db.close();
        }
    }
    public NotesDOA getNotesDOA(){
        return this.notesDOA;
    }
    public long saveTask(Note note){
        return this.notesDOA.save(note);
    }
    public boolean deleteTask(Note note){
        return this.notesDOA.delete(note);
    }
    public boolean UpdateTask(Note note){
        return this.notesDOA.update(note);
    }
    public Note getTask(long id){
        return this.notesDOA.get(id);
    }
    public ArrayList<Note> getAllTask(){
        return this.notesDOA.getAll();
    }
}
