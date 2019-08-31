package torach.java_conf.gr.jp.carmaintenance;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import java.util.Locale;

public class DateDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar calendar = Calendar.getInstance();
        return new DatePickerDialog(

                getActivity(),

                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        TextView date_picker = getActivity().findViewById(R.id.date_pickerShow);

                        date_picker.setText(
                                String.format(Locale.JAPAN, "%02d/%02d/%02d", year, monthOfYear + 1, dayOfMonth));
                    }


                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

    }
}
