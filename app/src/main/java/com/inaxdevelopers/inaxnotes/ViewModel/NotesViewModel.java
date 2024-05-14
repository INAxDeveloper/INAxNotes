package com.inaxdevelopers.inaxnotes.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.inaxdevelopers.inaxnotes.Repository.NotesRepository;
import com.inaxdevelopers.inaxnotes.model.Notes;

import java.util.List;


public class NotesViewModel extends AndroidViewModel {
    public LiveData<List<Notes>> getAllNotes;
    public LiveData<List<Notes>> hightolow;
    public LiveData<List<Notes>> lowtohigh;
    public NotesRepository repository;

    public NotesViewModel(Application application) {
        super(application);
        NotesRepository notesRepository = new NotesRepository(application);
        this.repository = notesRepository;
        this.getAllNotes = notesRepository.getallNotes;
        this.hightolow = this.repository.highToLow;
        this.lowtohigh = this.repository.lowToHigh;
    }

    public void insertNote(Notes notes) {
        this.repository.insertNotes(notes);
    }

    public void deleteNote(int id) {
        this.repository.deleteNotes(id);
    }

    public void updateNote(Notes notes) {
        this.repository.updateNotes(notes);
    }
}
