package com.androidfragmant.tourxyz.banglatourism.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;

import java.util.Calendar;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.layout_book_person)
public class BookingPersonActivity extends RoboAppCompatActivity {

    @InjectView(R.id.et_choose_date)
    private EditText chooseDate;

    private Calendar calendar;

    private int day;
    private int month;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        chooseDate.setText(day + "/" + month + "/" + "/" + year);

        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog();
            }
        });
    }

    public void DateDialog() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                chooseDate.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }
}
