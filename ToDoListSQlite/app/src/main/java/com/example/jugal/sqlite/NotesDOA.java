package com.example.jugal.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NotesDOA  {
    private SQLiteDatabase db;

    public NotesDOA(SQLiteDatabase db){
        this.db=db;

    }
    public long save(Note note)
    {
        ContentValues values = new ContentValues();
        values.put(NoteTable.COLUMN_TASK,note.getTask());
        values.put(NoteTable.COLUMN_PRIORITY,note.getPriority());
        values.put(NoteTable.COLUMN_STATUS,note.getStatus());
        values.put(NoteTable.COLUMN_TIME,note.getDate());
        values.put(NoteTable.COLUMN_POSITION,note.getPosition());
        return db.insert(NoteTable.TABLENAME,null,values);
    }
    public boolean update(Note note)
    {

        ContentValues values = new ContentValues();
        values.put(NoteTable.COLUMN_TASK,note.getTask());
        values.put(NoteTable.COLUMN_PRIORITY,note.getPriority());
        values.put(NoteTable.COLUMN_STATUS,note.getStatus());
        values.put(NoteTable.COLUMN_TIME,note.getDate());
        values.put(NoteTable.COLUMN_POSITION,note.getPosition());
        return db.update(NoteTable.TABLENAME,values,NoteTable.COLUMN_ID+"=?",new String[]{note.get_id()+""})>0;

    }
    public boolean delete(Note note)
    {
        Log.d("check","delete");
        return db.delete(NoteTable.TABLENAME,NoteTable.COLUMN_ID+"=?",new String[]{note.get_id()+""})>0;

    }
    public Note get(long id)
    {
        Note note = null;
        Cursor c = db.query(true,NoteTable.TABLENAME,new String[]
                {NoteTable.COLUMN_ID,NoteTable.COLUMN_TASK,NoteTable.COLUMN_PRIORITY,NoteTable.COLUMN_STATUS,NoteTable.COLUMN_TIME,NoteTable.COLUMN_POSITION},NoteTable.COLUMN_ID+"=?",new String[]{id+""},
                null,null,null,null);
        if(c!=null&& c.moveToFirst()){
            note = buildNoteFromCursor(c);
            if(!c.isClosed()) {
                    c.close();
            }

        }
        return note;

    }
    public ArrayList<Note> getAll()
    {
        ArrayList<Note> notes = new ArrayList<Note>();
        Cursor c = db.query(NoteTable.TABLENAME,new String[]
                        {NoteTable.COLUMN_ID,NoteTable.COLUMN_TASK,NoteTable.COLUMN_PRIORITY,NoteTable.COLUMN_STATUS,NoteTable.COLUMN_TIME,NoteTable.COLUMN_POSITION},
                        null,null,null,null,null);
        if(c!=null&& c.moveToFirst()){
            do {
                Note note = buildNoteFromCursor(c);
                notes.add(note);
            }while (c.moveToNext());
            if(!c.isClosed()) {
                c.close();
            }

        }
        return notes;

    }
    private Note buildNoteFromCursor(Cursor c){
        Note note = null;
        if(c!=null) {
            note = new Note();
            note.set_id(c.getLong(0));
            note.setTask(c.getString(1));
            note.setPriority(c.getString(2));
            note.setStatus(c.getInt(3));
            note.setDate(c.getString(4));
            note.setPosition(c.getInt(5));
        }
        return note;
        }
}

