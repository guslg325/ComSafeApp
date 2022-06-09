package Dialogs;

import androidx.fragment.app.*;
import android.app.Dialog;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.example.comsafe.R;

public class CreateDeleteConfirmationDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_update_car_confirm)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO update database (delete car)

                    }
                })
                .setNegativeButton(R.string.btnCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        //Dismiss dialog without changes
                        dismiss();
                    }
                })
                .setTitle(R.string.dialog_warning_title);
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
