package io.logicco.gnotes.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import io.logicco.gnotes.R;
import io.logicco.gnotes.dialogs.DeleteConfirmDialogFragment;
import io.logicco.gnotes.models.Note;
import io.logicco.gnotes.utils.ConfigUtil;
import io.logicco.gnotes.utils.Sort;
import io.logicco.gnotes.viewmodels.NotesViewModel;

public abstract class BaseNotesActivity extends AppCompatActivity
    implements DeleteConfirmDialogFragment.DeleteConfirmDialogListener{

    private static final String TAG_NOTE_DELETE_DIALOG = "delete_note_dialog";

    protected NotesViewModel _notesViewModel;
    protected RecyclerView _recyclerView;
    protected BaseNotesActivity.NotesAdapter _adapter;

    protected SharedPreferences _sharedPreferences;

    protected FloatingActionButton _addNoteFab;
    protected LinearLayout _blankLinearLayoutView;
    protected TextView _emptyMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        initNotes();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.sort_title:
                saveSortPref(Sort.TITLE);
                observeNotesData(Sort.TITLE);
                return true;
            case R.id.sort_added:
                saveSortPref(Sort.LAST_ADDED);
                observeNotesData(Sort.LAST_ADDED);
                return true;
            case R.id.sort_modified:
                saveSortPref(Sort.LAST_MODIFIED);
                observeNotesData(Sort.LAST_MODIFIED);
                return true;
            case R.id.archived_notes_button:
                startActivity(ArchivedNotesActivity.newIntent(this));
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void initNotes(){
        _recyclerView = findViewById(R.id.recycler_view_notes);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));

        _sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        _notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        _addNoteFab = findViewById(R.id.add_note_fab);
        _blankLinearLayoutView = findViewById(R.id.blank_linear_layout);
        _emptyMessageTextView = findViewById(R.id.empty_message_text_view);

        //Get saved sort-by pref
        Sort sortBy = Sort.valueOf(_sharedPreferences.getString("sort_by",
                ConfigUtil.getDefaultSortPref().toString()));
        observeNotesData(sortBy);

    }

    protected void updateNotesUi(List<Note> notes){
        _adapter = new NotesAdapter(notes);
        _recyclerView.setAdapter(_adapter);

        if (isListDataEmpty()){
            toggleBlankLinearLayout(View.VISIBLE);
        }else{
            toggleBlankLinearLayout(View.GONE);
        }
    }

    protected void toggleBlankLinearLayout(int visibility){
        _blankLinearLayoutView.setVisibility(visibility);

        if (visibility == View.VISIBLE){
            _emptyMessageTextView.setText(R.string.no_notes);
        }
    }

    protected boolean isListDataEmpty(){
        return _adapter.getItemCount() == 0;
    }

    protected void saveSortPref(Sort sort){
        SharedPreferences.Editor preferences = _sharedPreferences.edit();
        preferences.putString("sort_by", sort.toString());
        preferences.apply();
    }

    protected void denyAddNoteAction(){
        _addNoteFab.setVisibility(View.GONE);
    }

    protected void setActionBarTitle(String title){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(title);
    }

    /**
     * Must override to get the required notes list
     */
    public abstract void observeNotesData(Sort sortBy);


    private class NotesAdapter extends RecyclerView.Adapter<NoteHolder>{

        private List<Note> notes;

        public NotesAdapter(List<Note> notes){
            this.notes = notes;
        }

        @NonNull
        @Override
        public BaseNotesActivity.NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View notesItemsView = layoutInflater.inflate(R.layout.notes_items,parent,false);

            return new NoteHolder(notesItemsView);

        }

        @Override
        public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
            final Note note = notes.get(position);
            holder.bind(note);
        }

        @Override
        public int getItemCount() { return notes.size(); }

    }

    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Note note;
        private TextView noteName;
        private TextView noteContent;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            MaterialCardView materialCardView = itemView.findViewById(R.id.mcv);
            noteName = itemView.findViewById(R.id.title_text_view);
            noteContent = itemView.findViewById(R.id.content_text_view);

            materialCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(NoteActivity.newIntent(getBaseContext(), note.getId()));
                }
            });

            materialCardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    DeleteConfirmDialogFragment deleteDialog = new DeleteConfirmDialogFragment(note);
                    deleteDialog.show(getSupportFragmentManager(), TAG_NOTE_DELETE_DIALOG);
                    return true;
                }
            });


            itemView.setOnClickListener(this);
        }

        public void bind(Note note){
            this.note = note;
            noteName.setText(note.getTitle());
            if (!note.getContent().isEmpty())
                noteContent.setText(note.getContent());
            else
                noteContent.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            //
        }
    }

    @Override
    public void onDeleteDialogPositiveClick(DialogFragment dialog, Note note) {
        _notesViewModel.deleteNote(note);
    }

    @Override
    public void onDeleteDialogNegativeClick(DialogFragment dialog) { dialog.dismiss(); }

}
