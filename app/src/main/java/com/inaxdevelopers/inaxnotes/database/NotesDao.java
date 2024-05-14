package com.inaxdevelopers.inaxnotes.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.inaxdevelopers.inaxnotes.model.Notes;

import java.util.List;

@Dao
public interface NotesDao {
    @Query("DELETE FROM Notes WHERE id = :id")
    void deleteNotes(int id);

    @Query("SELECT * FROM Notes")
    LiveData<List<Notes>> getallNotes();
    @Query("SELECT * FROM Notes ORDER BY id DESC")
    LiveData<List<Notes>> highToLow();

    @Insert
    void insertNotes(Notes... notes);
    @Query("SELECT * FROM notes ORDER BY id ASC")
    LiveData<List<Notes>> lowtoHigh();

    @Update
    void updateNotes(Notes notes);
}
