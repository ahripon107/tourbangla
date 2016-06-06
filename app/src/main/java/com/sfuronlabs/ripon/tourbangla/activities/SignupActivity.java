package com.sfuronlabs.ripon.tourbangla.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.sfuronlabs.ripon.tourbangla.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ripon on 7/20/15.
 */
public class SignupActivity extends Activity {

    EditText fullName,userName,email,password;
    Button signup;
    TextView linktologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        fullName = (EditText) findViewById(R.id.reg_fullname);
        userName = (EditText) findViewById(R.id.reg_username);
        email = (EditText) findViewById(R.id.reg_email);
        password = (EditText) findViewById(R.id.reg_password);
        signup = (Button) findViewById(R.id.btnRegister);
        linktologin = (TextView) findViewById(R.id.link_to_login);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        linktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public void signUp() {
        String fullname = fullName.getText().toString().trim();
        String username = userName.getText().toString().trim();
        String Email = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        boolean validatioError= false;

        StringBuilder validatioErrorMessage = new StringBuilder();
        if(fullname.length()==0) {
            validatioError = true;
            validatioErrorMessage.append("Please enter your full name");
        }
        if (username.length()==0) {
            validatioError = true;
            validatioErrorMessage.append("Please enter your username");
        }
        if (Email.length()==0) {
            validatioError = true;
            validatioErrorMessage.append("Please enter your email");
        }
        if (pass.length()==0) {
            validatioError = true;
            validatioErrorMessage.append("Please enter your password");
        }
        if (pass.length()<6) {
            validatioError = true;
            validatioErrorMessage.append("Password must be atleast 6 character long");
        }
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = Email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (!matcher.matches()) {
            validatioError = true;
            validatioErrorMessage.append("Email address is not valid");
        }
        if (validatioError) {
            Toast.makeText(getApplicationContext(),validatioErrorMessage.toString(),Toast.LENGTH_LONG).show();
            return;
        }
        ParseUser user = new ParseUser();
        user.put("FullName",fullname);
        user.setUsername(username);
        user.setEmail(Email);
        user.setPassword(pass);
        final ProgressDialog dialog = ProgressDialog.show(SignupActivity.this, "Loading", "Please wait...", true);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                dialog.dismiss();
                if (e != null) {
                    Toast.makeText(SignupActivity.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                else {
                    finish();
                }
            }
        });
    }
}
