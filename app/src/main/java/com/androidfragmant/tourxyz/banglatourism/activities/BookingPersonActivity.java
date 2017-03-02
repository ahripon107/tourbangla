package com.androidfragmant.tourxyz.banglatourism.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.util.Validator;

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

    @InjectView(R.id.et_person_name)
    private EditText name;

    @InjectView(R.id.et_address)
    private EditText address;

    @InjectView(R.id.et_city)
    private EditText city;

    @InjectView(R.id.et_mobile_no)
    private EditText mobileno;

    @InjectView(R.id.et_no_of_entry)
    private EditText noOfEntry;

    @InjectView(R.id.tv_total)
    private TextView total;

    @InjectView(R.id.btn_book)
    private Button book;

    private Calendar calendar;

    private int day;
    private int month;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        chooseDate.setText(day + "/" + month + "/" + year);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog();
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validator.validateNotEmpty(chooseDate,"Required") && Validator.validateNotEmpty(name, "Required") && Validator.validateNotEmpty(address, "Required")
                        && Validator.validateNotEmpty(city, "Required") && Validator.validateNotEmpty(mobileno, "Required") && Validator.validateNotEmpty(noOfEntry, "Required")) {
                    Toast.makeText(getApplicationContext(), "Your booking has been successfully done", Toast.LENGTH_LONG).show();
                    chooseDate.getText().clear();
                    name.getText().clear();
                    address.getText().clear();
                    city.getText().clear();
                    mobileno.getText().clear();
                    noOfEntry.getText().clear();
                }
            }
        });

        noOfEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()==0) {
                    total.setText("Total tk. 0");
                } else {
                    int person = Integer.parseInt(noOfEntry.getText().toString().trim());
                    total.setText("Total tk. " + person*500);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}
