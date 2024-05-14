package com.inaxdevelopers.inaxnotes.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Notes {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "Priority")
    public String notesPriority;
    @ColumnInfo(name = "Title")
    public String notesTitle;
    @ColumnInfo(name = "SubTitle")
    public String notesSubtitle;
    @ColumnInfo(name = "Date")
    public String notesDate;
    @ColumnInfo(name = "NotesData")
    public String notes;
    @ColumnInfo(name = "ImagePath")
    public String notesImagePath;
    public String getNotesPriority() {
        return notesPriority;
    }

    public void setNotesPriority(String notesPriority) {
        this.notesPriority = notesPriority;
    }

    public String getNotesTitle() {
        return notesTitle;
    }

    public void setNotesTitle(String notesTitle) {
        this.notesTitle = notesTitle;
    }

    public String getNotesSubtitle() {
        return notesSubtitle;
    }

    public void setNotesSubtitle(String notesSubtitle) {
        this.notesSubtitle = notesSubtitle;
    }

    public String getNotesDate() {
        return notesDate;
    }

    public void setNotesDate(String notesDate) {
        this.notesDate = notesDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotesImagePath() {
        return notesImagePath;
    }

    public void setNotesImagePath(String notesImagePath) {
        this.notesImagePath = notesImagePath;
    }
}
