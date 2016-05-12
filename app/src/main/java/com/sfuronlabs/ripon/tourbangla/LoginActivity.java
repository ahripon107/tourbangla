package com.sfuronlabs.ripon.tourbangla;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by Ripon on 7/20/15.
 */
public class LoginActivity extends Activity {
    EditText UserName,Password;
    Button login;
    TextView linktoregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        UserName = (EditText) findViewById(R.id.etUsername);
        Password = (EditText) findViewById(R.id.etPassword);
        login = (Button) findViewById(R.id.btnLogin);
        linktoregister = (TextView) findViewById(R.id.link_to_register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogIn();
            }
        });

        linktoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void LogIn()
    {
        String username = UserName.getText().toString().trim();
        String password = Password.getText().toString().trim();
        boolean validatioError = false;
        StringBuilder validatioErrorMessage = new StringBuilder();
        if (username.length()==0)
        {
            validatioError = true;
            validatioErrorMessage.append("Please give your username");
        }
        if (password.length()==0)
        {
            validatioError = true;
            validatioErrorMessage.append("Please give your password");
        }
        if (validatioError)
        {
            Toast.makeText(getApplicationContext(),validatioErrorMessage.toString(),Toast.LENGTH_LONG).show();
            return;
        }

        final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "Loading", "Please wait...", true);
        ParseUser.logInInBackground(username,password,new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                dialog.dismiss();
                if (e != null)
                {
                    Toast.makeText(LoginActivity.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/
                    finish();
                }
            }
        });
    }
}
