package com.example.securevault.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.securevault.R;
import com.example.securevault.database.AppDatabase;
import com.example.securevault.models.PasswordEntry;
import com.example.securevault.utils.SessionManager;

public class AddPasswordActivity extends AppCompatActivity {

    private EditText etAppName;

    private EditText etUsername;

    private EditText etPassword;

    private Button btnSavePassword;

    private AppDatabase database;

    private SessionManager sessionManager;

    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_add_password
        );

        initializeViews();

        initializeSystem();

        btnSavePassword.setOnClickListener(v -> {

            savePassword();
        });
    }

    private void initializeViews() {

        etAppName =
                findViewById(R.id.etAppName);

        etUsername =
                findViewById(R.id.etUsername);

        etPassword =
                findViewById(R.id.etPassword);

        btnSavePassword =
                findViewById(R.id.btnSavePassword);
    }

    private void initializeSystem() {

        database =
                AppDatabase.getDatabase(this);

        sessionManager =
                new SessionManager(this);

        currentUserId =
                sessionManager.getUserId();
    }

    private void savePassword() {

        String appName =
                etAppName.getText()
                        .toString()
                        .trim();

        String username =
                etUsername.getText()
                        .toString()
                        .trim();

        String password =
                etPassword.getText()
                        .toString()
                        .trim();

        if (appName.isEmpty()
                || username.isEmpty()
                || password.isEmpty()) {

            Toast.makeText(
                    this,
                    "Fill all fields",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        PasswordEntry entry =
                new PasswordEntry(
                        currentUserId,
                        appName,
                        username,
                        password,
                        System.currentTimeMillis(),
                        database.passwordDao()
                                .getPasswordCount(currentUserId)
                );

        database.passwordDao().insert(entry);

        Toast.makeText(
                this,
                "Password Saved Successfully",
                Toast.LENGTH_SHORT
        ).show();

        finish();
    }
}