package com.androidfragmant.tourxyz.banglatourism.activities;

import android.os.Bundle;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.google.inject.Inject;
import com.androidfragmant.tourxyz.banglatourism.R;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.suggestnewplace)
public class SuggestNewPlaceActivity extends RoboAppCompatActivity {
    @InjectView(R.id.etSuggestedPlaceName)
    private EditText name;

    @InjectView(R.id.etSuggestedPlaceAddress)
    private EditText address;

    @InjectView(R.id.etSuggestedPlaceDivision)
    private EditText division;

    @InjectView(R.id.etSuggestedPlaceDescription)
    private EditText description;

    @InjectView(R.id.etSuggestedPlaceHowtogo)
    private EditText howtogo;

    @InjectView(R.id.etSuggestedPlaceHotels)
    private EditText hotels;

    @InjectView(R.id.etEmail)
    private EditText email;

    @InjectView(R.id.btnDoneSuggest)
    private Button suggestDone;

    @Inject
    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        suggestDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validator.validateNotEmpty(name, "Required") && Validator.validateNotEmpty(division, "Required") && Validator.validateNotEmpty(howtogo, "Required")) {

                    networkService.suggestNewPlace(hotels.getText().toString(), howtogo.getText().toString(),
                            description.getText().toString(), division.getText().toString(),
                            address.getText().toString(), name.getText().toString(),
                            email.getText().toString(), new DefaultMessageHandler(SuggestNewPlaceActivity.this, true) {
                                @Override
                                public void onSuccess(Message msg) {
                                    clearTextFields();
                                    Toast.makeText(SuggestNewPlaceActivity.this, "Thank you for your suggestion.", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }

    private void clearTextFields() {
        name.getText().clear();
        address.getText().clear();
        division.getText().clear();
        description.getText().clear();
        howtogo.getText().clear();
        hotels.getText().clear();
        email.getText().clear();
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
