package com.example.securevault.activities;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.securevault.R;
import com.example.securevault.database.AppDatabase;
import com.example.securevault.models.User;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private EditText etSecurityAnswer;
    private EditText etRecoveryPin;

    private Spinner spinnerSecurityQuestion;

    private Button btnCreateAccount;

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );

        setContentView(
                R.layout.activity_create_account
        );

        initializeViews();

        database = AppDatabase.getDatabase(this);

        setupSecurityQuestions();

        btnCreateAccount.setOnClickListener(v -> {

            createAccount();
        });
    }

    private void initializeViews() {

        etUsername = findViewById(R.id.etUsername);

        etPassword = findViewById(R.id.etPassword);

        etSecurityAnswer =
                findViewById(R.id.etSecurityAnswer);

        etRecoveryPin =
                findViewById(R.id.etRecoveryPin);

        spinnerSecurityQuestion =
                findViewById(R.id.spinnerSecurityQuestion);

        btnCreateAccount =
                findViewById(R.id.btnCreateAccount);
    }

    private void setupSecurityQuestions() {

        String[] questions = {

                "What is your pet name?",
                "What is your favourite food?",
                "What is your birth city?",
                "What is your best friend's name?",
                "What is your favourite movie?",
                "What is your dream job?",
                "What is your favourite sport?",
                "What was your first school name?",
                "What is your favourite car?",
                "What is your childhood nickname?",
                "What is your mother's maiden name?",
                "What is your favourite holiday destination?"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        questions
                );

        spinnerSecurityQuestion.setAdapter(adapter);
    }

    private void createAccount() {

        String username =
                etUsername.getText().toString().trim();

        String password =
                etPassword.getText().toString().trim();

        String securityQuestion =
                spinnerSecurityQuestion
                        .getSelectedItem()
                        .toString();

        String securityAnswer =
                etSecurityAnswer
                        .getText()
                        .toString()
                        .trim();

        String recoveryPin =
                etRecoveryPin
                        .getText()
                        .toString()
                        .trim();

        if (username.isEmpty()
                || password.isEmpty()
                || securityAnswer.isEmpty()
                || recoveryPin.isEmpty()) {

            Toast.makeText(
                    this,
                    "Fill all fields",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (recoveryPin.length() != 4) {

            Toast.makeText(
                    this,
                    "Recovery PIN must be 4 digits",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        User existingUser =
                database.userDao().getUser(username);

        if (existingUser != null) {

            Toast.makeText(
                    this,
                    "Username already exists",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        User user =
                new User(
                        username,
                        password,
                        securityQuestion,
                        securityAnswer,
                        recoveryPin
                );

        database.userDao().insert(user);

        Toast.makeText(
                this,
                "Account Created Successfully",
                Toast.LENGTH_LONG
        ).show();

        finish();
    }
}