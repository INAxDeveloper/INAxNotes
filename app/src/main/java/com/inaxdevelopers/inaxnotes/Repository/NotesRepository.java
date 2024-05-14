package com.inaxdevelopers.inaxnotes.Repository;

import android.app.Application;


import androidx.lifecycle.LiveData;

import com.inaxdevelopers.inaxnotes.database.NotesDao;
import com.inaxdevelopers.inaxnotes.database.NotesDatabase;
import com.inaxdevelopers.inaxnotes.model.Notes;

import java.util.List;


public class NotesRepository {
    public LiveData<List<Notes>> getallNotes;
    public LiveData<List<Notes>> highToLow;
    public LiveData<List<Notes>> lowToHigh;
    public NotesDao notesDao;

    public NotesRepository(Application application) {
        NotesDatabase database = NotesDatabase.getDatabaseInstance(application);
        NotesDao notesDao = database.notesDao();
        this.notesDao = notesDao;
        this.getallNotes = notesDao.getallNotes();
        this.highToLow = this.notesDao.highToLow();
        this.lowToHigh = this.notesDao.lowtoHigh();
    }

    public void insertNotes(Notes notes) {
        this.notesDao.insertNotes(notes);
    }

    public void deleteNotes(int id) {
        this.notesDao.deleteNotes(id);
    }

    public void updateNotes(Notes notes) {
        this.notesDao.updateNotes(notes);
    }
}
