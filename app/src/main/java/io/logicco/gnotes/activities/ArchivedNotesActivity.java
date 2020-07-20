package io.logicco.gnotes.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import java.util.List;

import io.logicco.gnotes.R;
import io.logicco.gnotes.models.Note;
import io.logicco.gnotes.utils.Sort;

public class ArchivedNotesActivity extends BaseNotesActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, ArchivedNotesActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActionBarTitle("Archived");
        denyAddNoteAction();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_archived_notes, menu);
        return true;
    }

    @Override
    public void observeNotesData(Sort sort) {
        switch (sort) {
            case TITLE:
                _notesViewModel.getArchivedNotesSortedByTitle().observe(
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
                _notesViewModel.getArchivedNotesSortedByCreatedDate().observe(
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
                _notesViewModel.getArchivedNotesSortedByUpdatedDate().observe(
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

}
