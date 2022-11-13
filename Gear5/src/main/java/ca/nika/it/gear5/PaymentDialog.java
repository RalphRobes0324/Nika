package ca.nika.it.gear5;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class PaymentDialog extends AppCompatDialogFragment {
    private EditText editTextNumber;
    private EditText editTextEXP;
    private EditText editTextCVV;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);

        builder.setView(view)
                .setTitle(R.string.payment)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.pay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        editTextNumber = view.findViewById(R.id.edit_card_num);
        editTextEXP = view.findViewById(R.id.edit_card_exp);
        editTextCVV = view.findViewById(R.id.edit_card_cvv);

        return builder.create();
    }
}
