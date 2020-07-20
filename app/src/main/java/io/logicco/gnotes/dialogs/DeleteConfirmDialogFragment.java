package io.logicco.gnotes.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import io.logicco.gnotes.R;
import io.logicco.gnotes.models.Note;

public class DeleteConfirmDialogFragment extends DialogFragment {

    private Note _note;

    public DeleteConfirmDialogFragment(Note note){
        _note = note;
    }

    public interface DeleteConfirmDialogListener {
        void onDeleteDialogPositiveClick(DialogFragment dialog, Note note);
        void onDeleteDialogNegativeClick(DialogFragment dialog);
    }

    DeleteConfirmDialogFragment.DeleteConfirmDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DeleteConfirmDialogFragment.DeleteConfirmDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement AddNoteDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());

        builder
                .setTitle(R.string.delete_note_confirm)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDeleteDialogPositiveClick(DeleteConfirmDialogFragment.this, _note);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDeleteDialogNegativeClick(DeleteConfirmDialogFragment.this);
                    }
                });
        return builder.create();
    }
}
