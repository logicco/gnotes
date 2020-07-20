package io.logicco.gnotes.database;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.UUID;

import io.logicco.gnotes.models.Note;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM notes WHERE archived = 0 ORDER BY created_at DESC")
    LiveData<List<Note>> getAllSortByCreatedDate();

    @Query("SELECT * FROM notes WHERE archived = 0 ORDER BY updated_at DESC")
    LiveData<List<Note>> getAllSortByUpdatedDate();

    @Query("SELECT * FROM notes WHERE archived = 0 ORDER BY title ASC")
    LiveData<List<Note>> getAllSortByTitle();

    @Query("SELECT * FROM notes WHERE archived = 1 ORDER BY title ASC")
    LiveData<List<Note>> getAllArchivedSortByTitle();

    @Query("SELECT * FROM notes WHERE archived = 1 ORDER BY created_at DESC")
    LiveData<List<Note>> getAllArchivedSortByCreatedDate();

    @Query("SELECT * FROM notes WHERE archived = 1 ORDER BY updated_at DESC")
    LiveData<List<Note>> getAllArchivedSortByUpdatedDate();

    @Query("SELECT * FROM notes where id = :noteId")
    LiveData<Note> findNoteById(UUID noteId);

    @Insert
    void insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);
}
