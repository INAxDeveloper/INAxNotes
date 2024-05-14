package com.inaxdevelopers.inaxnotes.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.inaxdevelopers.inaxnotes.model.Notes;

@Database(entities = {Notes.class}, version = 1, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {
    public static NotesDatabase INSTANCE;

    public abstract NotesDao notesDao();

    public static NotesDatabase getDatabaseInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = (NotesDatabase) Room.databaseBuilder(context.getApplicationContext(), NotesDatabase.class, "Notes_Database").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
