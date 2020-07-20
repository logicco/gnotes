package io.logicco.gnotes.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.logicco.gnotes.models.Note;
import io.logicco.gnotes.repositories.NotesRepository;
import io.logicco.gnotes.utils.Sort;

public class NotesViewModel extends ViewModel {

    private NotesRepository _notesRepository = NotesRepository.getInstance();

    private LiveData<List<Note>> _notesSortedByCreatedDate = _notesRepository.getNotes(Sort.LAST_ADDED);
    private LiveData<List<Note>> _notesSortedByTitle = _notesRepository.getNotes(Sort.TITLE);
    private LiveData<List<Note>> _notesSortedByUpdatedDate = _notesRepository.getNotes(Sort.LAST_MODIFIED);

    private LiveData<List<Note>> _archivedNotesSortedByTitle = _notesRepository.getArchivedNotes(Sort.TITLE);
    private LiveData<List<Note>> _archivedNotesSortedByCreatedDate = _notesRepository.getArchivedNotes(Sort.LAST_ADDED);
    private LiveData<List<Note>> _archivedNotesSortedByUpdatedDate = _notesRepository.getArchivedNotes(Sort.LAST_MODIFIED);

    public void addNote(Note note){
        _notesRepository.addNote(note);
    }

    public LiveData<List<Note>> getNotesSortedByCreatedDate() {
        return _notesSortedByCreatedDate;
    }

    public LiveData<List<Note>> getNotesSortedByTitle() {
        return _notesSortedByTitle;
    }

    public LiveData<List<Note>> getNotesSortedByUpdatedDate() {
        return _notesSortedByUpdatedDate;
    }

    public LiveData<List<Note>> getArchivedNotesSortedByTitle() {
        return _archivedNotesSortedByTitle;
    }

    public LiveData<List<Note>> getArchivedNotesSortedByUpdatedDate() {
        return _archivedNotesSortedByUpdatedDate;
    }

    public LiveData<List<Note>> getArchivedNotesSortedByCreatedDate() {
        return _archivedNotesSortedByCreatedDate;
    }


    public void deleteNote(Note note) { _notesRepository.deleteNote(note); }
}
