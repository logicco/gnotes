package io.logicco.gnotes.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;


import java.util.List;

import io.logicco.gnotes.R;
import io.logicco.gnotes.dialogs.NoteDialogFragment;
import io.logicco.gnotes.models.Note;
import io.logicco.gnotes.utils.Sort;
import io.logicco.gnotes.utils.TextUtil;

public class NotesActivity extends BaseNotesActivity
        implements NoteDialogFragment.NoteDialogListener {

    private static final String TAG_NOTE_CREATE_DIALOG = "create_note_dialog";

    public static Intent newIntent(Context context) {
        return new Intent(context, NotesActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _addNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteDialogFragment noteDialog = new NoteDialogFragment();
                noteDialog.show(getSupportFragmentManager(), TAG_NOTE_CREATE_DIALOG);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_notes, menu);
        return true;
    }


    @Override
    public void observeNotesData(Sort sort) {
        switch (sort) {
            case TITLE:
                _notesViewModel.getNotesSortedByTitle().observe(
                        this,
                        new Observer<List<Note>>() {
                            @Override
                            public void onChanged(List<Note> notes) {
                                updateNotesUi(notes);
                            }
                        }
                );
                break;
            case LAST_ADDED:
                _notesViewModel.getNotesSortedByCreatedDate().observe(
                        this,
                        new Observer<List<Note>>() {
                            @Override
                            public void onChanged(List<Note> notes) {
                                updateNotesUi(notes);
                            }
                        }
                );
                break;
            case LAST_MODIFIED:
                _notesViewModel.getNotesSortedByUpdatedDate().observe(
                        this,
                        new Observer<List<Note>>() {
                            @Override
                            public void onChanged(List<Note> notes) {
                                updateNotesUi(notes);
                            }
                        }
                );
                break;
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String title) {
        if (title.isEmpty()) {
            TextUtil.toast(this, "Empty title");
            return;
        }

        Note note = new Note(title);
        _notesViewModel.addNote(note);
        startActivity(NoteActivity.newIntent(this, note.getId()));

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

}
