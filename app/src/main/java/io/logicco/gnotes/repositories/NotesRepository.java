package io.logicco.gnotes.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.logicco.gnotes.database.NotesDao;
import io.logicco.gnotes.database.NotesDatabase;
import io.logicco.gnotes.models.Note;
import io.logicco.gnotes.utils.Sort;

public final class NotesRepository {

    private static NotesRepository _INSTANCE = null;
    private NotesDao _notesDao;
    private Executor _executor = Executors.newSingleThreadExecutor();

    private NotesRepository(Context context) {
        NotesDatabase notesDatabase = Room.databaseBuilder(
                context.getApplicationContext(), NotesDatabase.class, NotesDatabase.DATABASE_NAME
        ).build();
        _notesDao = notesDatabase.noteDao();
    }

    public LiveData<List<Note>> getNotes(Sort sort) {
        switch (sort) {
            case TITLE:
                return _notesDao.getAllSortByTitle();
            case LAST_ADDED:
                return _notesDao.getAllSortByCreatedDate();
            case LAST_MODIFIED:
                return _notesDao.getAllSortByUpdatedDate();
        }
        return _notesDao.getAllSortByUpdatedDate();
    }

    public LiveData<List<Note>> getArchivedNotes(Sort sort) {
        switch (sort) {
            case TITLE:
                return _notesDao.getAllArchivedSortByTitle();
            case LAST_ADDED:
                return _notesDao.getAllArchivedSortByCreatedDate();
            case LAST_MODIFIED:
                return _notesDao.getAllArchivedSortByUpdatedDate();
        }
        return _notesDao.getAllArchivedSortByUpdatedDate();
    }


    public void addNote(final Note note) {
        _executor.execute(new Runnable() {
            @Override
            public void run() {
                _notesDao.insertNote(note);
            }
        });
    }

    public void deleteNote(final Note note) {
        _executor.execute(new Runnable() {
            @Override
            public void run() {
                _notesDao.deleteNote(note);
            }
        });
    }

    public void updateNote(final Note note) {
        _executor.execute(new Runnable() {
            @Override
            public void run() {
                note.setUpdatedAt(new Date());
                _notesDao.updateNote(note);
            }
        });
    }

    /**
     * Update note only if its content is changed
     */
    public void updateNote(final Note noteToUpdate, final String oldContent) {
        _executor.execute(new Runnable() {
            @Override
            public void run() {
                if (!noteToUpdate.getContent().equals(oldContent)) {
                    noteToUpdate.setUpdatedAt(new Date());
                    _notesDao.updateNote(noteToUpdate);
                }
            }
        });
    }

    public LiveData<Note> getNote(UUID noteId) {
        return _notesDao.findNoteById(noteId);
    }

    public static void initialize(Context context) {
        if (_INSTANCE == null) {
            _INSTANCE = new NotesRepository(context);
        }
    }

    /**
     * NotesRepository must be initialized before calling getInstance()
     */
    public static NotesRepository getInstance() throws IllegalStateException {
        if (_INSTANCE == null)
            throw new IllegalStateException("NotesRepository must be initialized before calling getInstance");
        else
            return _INSTANCE;
    }


}
