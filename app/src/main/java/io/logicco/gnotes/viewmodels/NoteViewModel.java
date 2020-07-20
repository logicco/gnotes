package io.logicco.gnotes.viewmodels;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.UUID;

import io.logicco.gnotes.models.Note;
import io.logicco.gnotes.repositories.NotesRepository;

public class NoteViewModel extends ViewModel {

    private NotesRepository _notesRepository = NotesRepository.getInstance();
    private MutableLiveData<UUID> _noteIdLiveData = new MutableLiveData<>();

    private LiveData<Note> _noteLiveData = Transformations.switchMap(_noteIdLiveData,
            new Function<UUID, LiveData<Note>>() {
                @Override
                public LiveData<Note> apply(UUID id) {
                    return _notesRepository.getNote(id);
                }
    });

    public LiveData<Note> getNoteLiveData() {
        return _noteLiveData;
    }

    public void loadNote(UUID noteId){
        _noteIdLiveData.setValue(noteId);
    }

    public void deleteNote(Note note) { _notesRepository.deleteNote(note); }

    public void saveNote(Note note){
        _notesRepository.updateNote(note);
    }

    public void saveNote(Note noteToUpdate, String oldContent) { _notesRepository.updateNote(noteToUpdate, oldContent); }
}
