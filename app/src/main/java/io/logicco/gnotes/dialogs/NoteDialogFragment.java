package io.logicco.gnotes.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import io.logicco.gnotes.R;

public class NoteDialogFragment extends DialogFragment {

    private String _title;

    public NoteDialogFragment(){}

    public NoteDialogFragment(String title){
        _title = title;
    }

    public interface NoteDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, String title);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    NoteDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (NoteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoteDialogListener");
        }
    }

    private EditText _titleField;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());

        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_note_create_update,null);

        _titleField = view.findViewById(R.id.title_field);
        _titleField.setText(_title);

        builder
                .setView(view)
                .setPositiveButton(R.string.save_note, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogPositiveClick(NoteDialogFragment.this,
                                _titleField.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick(NoteDialogFragment.this);
                    }
                });
        return builder.create();
    }
}
