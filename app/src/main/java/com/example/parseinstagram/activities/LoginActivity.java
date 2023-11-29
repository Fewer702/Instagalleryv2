package com.example.parseinstagram.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.parseinstagram.MainActivity;
import com.example.parseinstagram.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";
    private EditText userLog;
    private EditText passLog;
    private Button logBtn;
    private Button signUpBtn;
    private ImageView img;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userLog = findViewById(R.id.usernameLog);
        passLog = findViewById(R.id.passwrdLog);
        logBtn = findViewById(R.id.buttonLog);
        signUpBtn = findViewById(R.id.buttonSignUp);
        img = findViewById(R.id.logoImage);

        if(ParseUser.getCurrentUser() != null){
            toMainActivity();
        }

        Glide.with(this)
                .load(R.drawable.insta)
                .into(img);

        // login button click
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick button");
                String username = userLog.getText().toString();
                String passsword = passLog.getText().toString();
                loginUser(username, passsword);
            }
        });

        // signup button click
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick signUp button");
                String username = userLog.getText().toString();
                String password = passLog.getText().toString();
                signUpUser(username, password);
            }
        });
    }

    private void signUpUser(String username, String password) {
        Log.i(TAG, "Attempting to user signIn " + username);
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with signUp", e);
                    Toast.makeText(LoginActivity.this, "Issue with signUp", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Go to the main activity
                toMainActivity();
            }
        });
    }

    private void loginUser(String username, String password){
        Log.i(TAG, "Attempting to login user " + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(LoginActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT);
                    return;
                }
                // Go to the main activity
                toMainActivity();
                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT);
            }
        });

    }

    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}