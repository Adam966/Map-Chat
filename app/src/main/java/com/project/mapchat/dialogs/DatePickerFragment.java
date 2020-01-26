package com.project.mapchat.dialogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.DatePicker;

import java.time.LocalDateTime;
import java.util.Date;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private DialogListener dialogListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dialogListener = (DialogListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        if (c.getTime().compareTo(new Date()) < 0)
            dialogListener.getDate(c.getTime());

    }

    public interface DialogListener {
        void getDate(Date date);
    }
}
