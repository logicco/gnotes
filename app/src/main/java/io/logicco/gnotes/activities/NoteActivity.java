package io.logicco.gnotes.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.UUID;

import io.logicco.gnotes.R;
import io.logicco.gnotes.dialogs.DeleteConfirmDialogFragment;
import io.logicco.gnotes.dialogs.NoteDialogFragment;
import io.logicco.gnotes.models.Note;
import io.logicco.gnotes.utils.TextUtil;
import io.logicco.gnotes.viewmodels.NoteViewModel;

public class NoteActivity extends AppCompatActivity
        implements DeleteConfirmDialogFragment.DeleteConfirmDialogListener,
        NoteDialogFragment.NoteDialogListener {

    private static final String EXTRA_NOTE_ID = "note_id";
    private static final String TAG_DELETE_NOTE = "delete_note";
    private static final String TAG_UPDATE_NOTE = "update_note";

    private EditText _contentField;
    private Note _note;
    private String _oldContent;

    private NoteViewModel _noteViewModel;

    public static Intent newIntent(Context context, UUID noteId){
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        init();
    }

    private void init(){
        _contentField = findViewById(R.id.content_field);

        _noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        UUID noteId = (UUID) getIntent().getSerializableExtra(EXTRA_NOTE_ID);
        _noteViewModel.loadNote(noteId);

        _noteViewModel.getNoteLiveData().observe(
                this,
                new Observer<Note>() {
                    @Override
                    public void onChanged(Note note) {
                        _note = note;
                        _oldContent = note.getContent();
                        updateUi();
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_note,menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!_note.isArchived()) {
            //show archive button
            MenuItem archiveButton = menu.findItem(R.id.archive_button);
            archiveButton.setVisible(true);
        } else {
            //show unarchive button
            MenuItem unArchiveButton = menu.findItem(R.id.unarchive_button);
            unArchiveButton.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.archive_button:
                TextUtil.toast(this,"Archived");
                toggleArchive(true);
                return true;
            case R.id.unarchive_button:
                TextUtil.toast(this,"Unarchived");
                toggleArchive(false);
                return true;
            case R.id.delete_note:
                DeleteConfirmDialogFragment deleteDialog = new DeleteConfirmDialogFragment(_note);
                deleteDialog.show(getSupportFragmentManager(), TAG_DELETE_NOTE);
                return true;
            case R.id.edit_title:
                NoteDialogFragment updateDialog = new NoteDialogFragment(_note.getTitle());
                updateDialog.show(getSupportFragmentManager(), TAG_UPDATE_NOTE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        //Only update note if its content is changed
        _noteViewModel.saveNote(_note, _oldContent);
    }

    @Override
    public void onStart(){
        super.onStart();

        _contentField.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                _note.setContent(s.toString());
            }
        });
    }


    private void updateUi(){
        if (_note != null){
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null){
                actionBar.setTitle(_note.getTitle());
            }

            _contentField.setText(_note.getContent());
        }
    }

    private void toggleArchive(boolean value){
        _note.setArchived(value);
        _noteViewModel.saveNote(_note);
        invalidateOptionsMenu();
    }

    @Override
    public void onDeleteDialogPositiveClick(DialogFragment dialog, Note note) {
        _noteViewModel.deleteNote(_note);
        startActivity(NotesActivity.newIntent(this));
    }

    @Override
    public void onDeleteDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String title) {
        if (!title.isEmpty()){
            _note.setTitle(title);
            _noteViewModel.saveNote(_note);
            updateUi();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}