package agtzm.kapelo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import java.util.Calendar;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

/**
 * Created by dell on 25/01/2018.
 */

@SuppressLint("ValidFragment")
public class fecha extends android.support.v4.app.DialogFragment implements DatePickerDialog.OnDateSetListener {

    EditText txtdate;
    public fecha(View view){
        txtdate=(EditText)view;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {


        // Use the current date as the default date in the dialog
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);


    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        //show to the selected date in the text box
        String date=day+"-"+(month+1)+"-"+year;
        txtdate.setText(date);
    }
}