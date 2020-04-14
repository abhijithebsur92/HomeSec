package com.comp6441.homesec;

import com.google.android.material.textfield.TextInputLayout;

import com.example.homesec.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText mUserNameEditText;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final TextInputLayout mUserNameTextInputLayout = findViewById(R.id.username_input_layout);
        final TextInputLayout mPasswordTextInputLayout = findViewById(R.id.password_input_layout);

        mUserNameEditText = findViewById(R.id.username_editText);
        mPasswordEditText = findViewById(R.id.password_editText);

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                String name = mUserNameEditText.getText() != null ? mUserNameEditText.getText().toString() : "";
                String password = mPasswordEditText.getText() != null ? mPasswordEditText.getText().toString() : "";

                if (TextUtils.isEmpty(name)) {
                    mUserNameTextInputLayout.setError("Username cannot be empty");
                } else if(TextUtils.isEmpty(password)) {
                    mUserNameTextInputLayout.setError("");
                    mPasswordTextInputLayout.setError("Password cannot be empty");
                } else {
                    mUserNameTextInputLayout.setError("");
                    mPasswordTextInputLayout.setError("");
                    launchHomeScreen();
                }
            }
        });
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
