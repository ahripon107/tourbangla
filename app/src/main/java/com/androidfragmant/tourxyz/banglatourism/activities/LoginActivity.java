package com.androidfragmant.tourxyz.banglatourism.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.model.LoginEvent;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Ripon
 */

public class LoginActivity extends AppCompatActivity{

    LoginButton loginButton;
    CallbackManager callbackManager;
    Profile profile;
    TextView textView;
    ProfileTracker profileTracker;
    AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        textView =(TextView) findViewById(R.id.login_text);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");


        callbackManager = CallbackManager.Factory.create();

        profile = Profile.getCurrentProfile();
        if (profile == null) {
            textView.setText("Please Log In");
        } else {
            textView.setText("You are logged in as "+profile.getName());
        }
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this,"Login Success",Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this,"Login cancel",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this,"Login error",Toast.LENGTH_LONG).show();
            }
        });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                if (currentProfile == null) {
                    textView.setText("Please Log In");
                } else {
                    textView.setText("You are logged in as "+currentProfile.getName());
                    EventBus.getDefault().post(new LoginEvent());
                }
            }
        };

        profileTracker.startTracking();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                    EventBus.getDefault().post(new LoginEvent());

            }
        };

        accessTokenTracker.startTracking();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        profileTracker.startTracking();
        accessTokenTracker.stopTracking();
        super.onDestroy();
    }
}
